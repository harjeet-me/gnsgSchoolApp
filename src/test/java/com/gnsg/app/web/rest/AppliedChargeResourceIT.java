package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.AppliedCharge;
import com.gnsg.app.repository.AppliedChargeRepository;
import com.gnsg.app.repository.search.AppliedChargeSearchRepository;
import com.gnsg.app.service.AppliedChargeService;
import com.gnsg.app.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.gnsg.app.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link AppliedChargeResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
public class AppliedChargeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Double DEFAULT_AMMOUNT = 1D;
    private static final Double UPDATED_AMMOUNT = 2D;

    private static final Integer DEFAULT_DUE_INTERVAL = 1;
    private static final Integer UPDATED_DUE_INTERVAL = 2;

    @Autowired
    private AppliedChargeRepository appliedChargeRepository;

    @Autowired
    private AppliedChargeService appliedChargeService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.AppliedChargeSearchRepositoryMockConfiguration
     */
    @Autowired
    private AppliedChargeSearchRepository mockAppliedChargeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restAppliedChargeMockMvc;

    private AppliedCharge appliedCharge;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppliedChargeResource appliedChargeResource = new AppliedChargeResource(appliedChargeService);
        this.restAppliedChargeMockMvc = MockMvcBuilders.standaloneSetup(appliedChargeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppliedCharge createEntity(EntityManager em) {
        AppliedCharge appliedCharge = new AppliedCharge()
            .type(DEFAULT_TYPE)
            .ammount(DEFAULT_AMMOUNT)
            .dueInterval(DEFAULT_DUE_INTERVAL);
        return appliedCharge;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppliedCharge createUpdatedEntity(EntityManager em) {
        AppliedCharge appliedCharge = new AppliedCharge()
            .type(UPDATED_TYPE)
            .ammount(UPDATED_AMMOUNT)
            .dueInterval(UPDATED_DUE_INTERVAL);
        return appliedCharge;
    }

    @BeforeEach
    public void initTest() {
        appliedCharge = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppliedCharge() throws Exception {
        int databaseSizeBeforeCreate = appliedChargeRepository.findAll().size();

        // Create the AppliedCharge
        restAppliedChargeMockMvc.perform(post("/api/applied-charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appliedCharge)))
            .andExpect(status().isCreated());

        // Validate the AppliedCharge in the database
        List<AppliedCharge> appliedChargeList = appliedChargeRepository.findAll();
        assertThat(appliedChargeList).hasSize(databaseSizeBeforeCreate + 1);
        AppliedCharge testAppliedCharge = appliedChargeList.get(appliedChargeList.size() - 1);
        assertThat(testAppliedCharge.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAppliedCharge.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testAppliedCharge.getDueInterval()).isEqualTo(DEFAULT_DUE_INTERVAL);

        // Validate the AppliedCharge in Elasticsearch
        verify(mockAppliedChargeSearchRepository, times(1)).save(testAppliedCharge);
    }

    @Test
    @Transactional
    public void createAppliedChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appliedChargeRepository.findAll().size();

        // Create the AppliedCharge with an existing ID
        appliedCharge.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppliedChargeMockMvc.perform(post("/api/applied-charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appliedCharge)))
            .andExpect(status().isBadRequest());

        // Validate the AppliedCharge in the database
        List<AppliedCharge> appliedChargeList = appliedChargeRepository.findAll();
        assertThat(appliedChargeList).hasSize(databaseSizeBeforeCreate);

        // Validate the AppliedCharge in Elasticsearch
        verify(mockAppliedChargeSearchRepository, times(0)).save(appliedCharge);
    }


    @Test
    @Transactional
    public void getAllAppliedCharges() throws Exception {
        // Initialize the database
        appliedChargeRepository.saveAndFlush(appliedCharge);

        // Get all the appliedChargeList
        restAppliedChargeMockMvc.perform(get("/api/applied-charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appliedCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dueInterval").value(hasItem(DEFAULT_DUE_INTERVAL)));
    }
    
    @Test
    @Transactional
    public void getAppliedCharge() throws Exception {
        // Initialize the database
        appliedChargeRepository.saveAndFlush(appliedCharge);

        // Get the appliedCharge
        restAppliedChargeMockMvc.perform(get("/api/applied-charges/{id}", appliedCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(appliedCharge.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.doubleValue()))
            .andExpect(jsonPath("$.dueInterval").value(DEFAULT_DUE_INTERVAL));
    }

    @Test
    @Transactional
    public void getNonExistingAppliedCharge() throws Exception {
        // Get the appliedCharge
        restAppliedChargeMockMvc.perform(get("/api/applied-charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppliedCharge() throws Exception {
        // Initialize the database
        appliedChargeService.save(appliedCharge);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockAppliedChargeSearchRepository);

        int databaseSizeBeforeUpdate = appliedChargeRepository.findAll().size();

        // Update the appliedCharge
        AppliedCharge updatedAppliedCharge = appliedChargeRepository.findById(appliedCharge.getId()).get();
        // Disconnect from session so that the updates on updatedAppliedCharge are not directly saved in db
        em.detach(updatedAppliedCharge);
        updatedAppliedCharge
            .type(UPDATED_TYPE)
            .ammount(UPDATED_AMMOUNT)
            .dueInterval(UPDATED_DUE_INTERVAL);

        restAppliedChargeMockMvc.perform(put("/api/applied-charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppliedCharge)))
            .andExpect(status().isOk());

        // Validate the AppliedCharge in the database
        List<AppliedCharge> appliedChargeList = appliedChargeRepository.findAll();
        assertThat(appliedChargeList).hasSize(databaseSizeBeforeUpdate);
        AppliedCharge testAppliedCharge = appliedChargeList.get(appliedChargeList.size() - 1);
        assertThat(testAppliedCharge.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAppliedCharge.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testAppliedCharge.getDueInterval()).isEqualTo(UPDATED_DUE_INTERVAL);

        // Validate the AppliedCharge in Elasticsearch
        verify(mockAppliedChargeSearchRepository, times(1)).save(testAppliedCharge);
    }

    @Test
    @Transactional
    public void updateNonExistingAppliedCharge() throws Exception {
        int databaseSizeBeforeUpdate = appliedChargeRepository.findAll().size();

        // Create the AppliedCharge

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppliedChargeMockMvc.perform(put("/api/applied-charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(appliedCharge)))
            .andExpect(status().isBadRequest());

        // Validate the AppliedCharge in the database
        List<AppliedCharge> appliedChargeList = appliedChargeRepository.findAll();
        assertThat(appliedChargeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the AppliedCharge in Elasticsearch
        verify(mockAppliedChargeSearchRepository, times(0)).save(appliedCharge);
    }

    @Test
    @Transactional
    public void deleteAppliedCharge() throws Exception {
        // Initialize the database
        appliedChargeService.save(appliedCharge);

        int databaseSizeBeforeDelete = appliedChargeRepository.findAll().size();

        // Delete the appliedCharge
        restAppliedChargeMockMvc.perform(delete("/api/applied-charges/{id}", appliedCharge.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AppliedCharge> appliedChargeList = appliedChargeRepository.findAll();
        assertThat(appliedChargeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the AppliedCharge in Elasticsearch
        verify(mockAppliedChargeSearchRepository, times(1)).deleteById(appliedCharge.getId());
    }

    @Test
    @Transactional
    public void searchAppliedCharge() throws Exception {
        // Initialize the database
        appliedChargeService.save(appliedCharge);
        when(mockAppliedChargeSearchRepository.search(queryStringQuery("id:" + appliedCharge.getId())))
            .thenReturn(Collections.singletonList(appliedCharge));
        // Search the appliedCharge
        restAppliedChargeMockMvc.perform(get("/api/_search/applied-charges?query=id:" + appliedCharge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appliedCharge.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dueInterval").value(hasItem(DEFAULT_DUE_INTERVAL)));
    }
}
