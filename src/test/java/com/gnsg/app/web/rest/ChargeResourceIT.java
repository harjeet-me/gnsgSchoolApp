package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.Charge;
import com.gnsg.app.repository.ChargeRepository;
import com.gnsg.app.repository.search.ChargeSearchRepository;
import com.gnsg.app.service.ChargeService;
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
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ChargeResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
public class ChargeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_AMMOUNT = 1D;
    private static final Double UPDATED_AMMOUNT = 2D;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PAYMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PAYMENT_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_AMMOUNT_PAID = 1D;
    private static final Double UPDATED_AMMOUNT_PAID = 2D;

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    @Autowired
    private ChargeRepository chargeRepository;

    @Autowired
    private ChargeService chargeService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.ChargeSearchRepositoryMockConfiguration
     */
    @Autowired
    private ChargeSearchRepository mockChargeSearchRepository;

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

    private MockMvc restChargeMockMvc;

    private Charge charge;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChargeResource chargeResource = new ChargeResource(chargeService);
        this.restChargeMockMvc = MockMvcBuilders.standaloneSetup(chargeResource)
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
    public static Charge createEntity(EntityManager em) {
        Charge charge = new Charge()
            .name(DEFAULT_NAME)
            .ammount(DEFAULT_AMMOUNT)
            .dueDate(DEFAULT_DUE_DATE)
            .paymentDate(DEFAULT_PAYMENT_DATE)
            .ammountPaid(DEFAULT_AMMOUNT_PAID)
            .ref(DEFAULT_REF);
        return charge;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Charge createUpdatedEntity(EntityManager em) {
        Charge charge = new Charge()
            .name(UPDATED_NAME)
            .ammount(UPDATED_AMMOUNT)
            .dueDate(UPDATED_DUE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .ammountPaid(UPDATED_AMMOUNT_PAID)
            .ref(UPDATED_REF);
        return charge;
    }

    @BeforeEach
    public void initTest() {
        charge = createEntity(em);
    }

    @Test
    @Transactional
    public void createCharge() throws Exception {
        int databaseSizeBeforeCreate = chargeRepository.findAll().size();

        // Create the Charge
        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charge)))
            .andExpect(status().isCreated());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeCreate + 1);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCharge.getAmmount()).isEqualTo(DEFAULT_AMMOUNT);
        assertThat(testCharge.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testCharge.getPaymentDate()).isEqualTo(DEFAULT_PAYMENT_DATE);
        assertThat(testCharge.getAmmountPaid()).isEqualTo(DEFAULT_AMMOUNT_PAID);
        assertThat(testCharge.getRef()).isEqualTo(DEFAULT_REF);

        // Validate the Charge in Elasticsearch
        verify(mockChargeSearchRepository, times(1)).save(testCharge);
    }

    @Test
    @Transactional
    public void createChargeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chargeRepository.findAll().size();

        // Create the Charge with an existing ID
        charge.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChargeMockMvc.perform(post("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charge)))
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeCreate);

        // Validate the Charge in Elasticsearch
        verify(mockChargeSearchRepository, times(0)).save(charge);
    }


    @Test
    @Transactional
    public void getAllCharges() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get all the chargeList
        restChargeMockMvc.perform(get("/api/charges?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charge.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].ammountPaid").value(hasItem(DEFAULT_AMMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)));
    }
    
    @Test
    @Transactional
    public void getCharge() throws Exception {
        // Initialize the database
        chargeRepository.saveAndFlush(charge);

        // Get the charge
        restChargeMockMvc.perform(get("/api/charges/{id}", charge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(charge.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ammount").value(DEFAULT_AMMOUNT.doubleValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.paymentDate").value(DEFAULT_PAYMENT_DATE.toString()))
            .andExpect(jsonPath("$.ammountPaid").value(DEFAULT_AMMOUNT_PAID.doubleValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF));
    }

    @Test
    @Transactional
    public void getNonExistingCharge() throws Exception {
        // Get the charge
        restChargeMockMvc.perform(get("/api/charges/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCharge() throws Exception {
        // Initialize the database
        chargeService.save(charge);
        // As the test used the service layer, reset the Elasticsearch mock repository
        reset(mockChargeSearchRepository);

        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Update the charge
        Charge updatedCharge = chargeRepository.findById(charge.getId()).get();
        // Disconnect from session so that the updates on updatedCharge are not directly saved in db
        em.detach(updatedCharge);
        updatedCharge
            .name(UPDATED_NAME)
            .ammount(UPDATED_AMMOUNT)
            .dueDate(UPDATED_DUE_DATE)
            .paymentDate(UPDATED_PAYMENT_DATE)
            .ammountPaid(UPDATED_AMMOUNT_PAID)
            .ref(UPDATED_REF);

        restChargeMockMvc.perform(put("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCharge)))
            .andExpect(status().isOk());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);
        Charge testCharge = chargeList.get(chargeList.size() - 1);
        assertThat(testCharge.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCharge.getAmmount()).isEqualTo(UPDATED_AMMOUNT);
        assertThat(testCharge.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testCharge.getPaymentDate()).isEqualTo(UPDATED_PAYMENT_DATE);
        assertThat(testCharge.getAmmountPaid()).isEqualTo(UPDATED_AMMOUNT_PAID);
        assertThat(testCharge.getRef()).isEqualTo(UPDATED_REF);

        // Validate the Charge in Elasticsearch
        verify(mockChargeSearchRepository, times(1)).save(testCharge);
    }

    @Test
    @Transactional
    public void updateNonExistingCharge() throws Exception {
        int databaseSizeBeforeUpdate = chargeRepository.findAll().size();

        // Create the Charge

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChargeMockMvc.perform(put("/api/charges")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(charge)))
            .andExpect(status().isBadRequest());

        // Validate the Charge in the database
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Charge in Elasticsearch
        verify(mockChargeSearchRepository, times(0)).save(charge);
    }

    @Test
    @Transactional
    public void deleteCharge() throws Exception {
        // Initialize the database
        chargeService.save(charge);

        int databaseSizeBeforeDelete = chargeRepository.findAll().size();

        // Delete the charge
        restChargeMockMvc.perform(delete("/api/charges/{id}", charge.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Charge> chargeList = chargeRepository.findAll();
        assertThat(chargeList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Charge in Elasticsearch
        verify(mockChargeSearchRepository, times(1)).deleteById(charge.getId());
    }

    @Test
    @Transactional
    public void searchCharge() throws Exception {
        // Initialize the database
        chargeService.save(charge);
        when(mockChargeSearchRepository.search(queryStringQuery("id:" + charge.getId())))
            .thenReturn(Collections.singletonList(charge));
        // Search the charge
        restChargeMockMvc.perform(get("/api/_search/charges?query=id:" + charge.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(charge.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ammount").value(hasItem(DEFAULT_AMMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].paymentDate").value(hasItem(DEFAULT_PAYMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].ammountPaid").value(hasItem(DEFAULT_AMMOUNT_PAID.doubleValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)));
    }
}
