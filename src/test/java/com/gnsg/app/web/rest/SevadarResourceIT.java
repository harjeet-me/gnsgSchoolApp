package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.Sevadar;
import com.gnsg.app.repository.SevadarRepository;
import com.gnsg.app.repository.search.SevadarSearchRepository;
import com.gnsg.app.service.SevadarService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SevadarResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SevadarResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Instant DEFAULT_SEVA_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEVA_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SEVA_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEVA_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_VALID = false;
    private static final Boolean UPDATED_IS_VALID = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private SevadarRepository sevadarRepository;

    @Autowired
    private SevadarService sevadarService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.SevadarSearchRepositoryMockConfiguration
     */
    @Autowired
    private SevadarSearchRepository mockSevadarSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSevadarMockMvc;

    private Sevadar sevadar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sevadar createEntity(EntityManager em) {
        Sevadar sevadar = new Sevadar()
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .sevaStartDate(DEFAULT_SEVA_START_DATE)
            .sevaEndDate(DEFAULT_SEVA_END_DATE)
            .isValid(DEFAULT_IS_VALID)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return sevadar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sevadar createUpdatedEntity(EntityManager em) {
        Sevadar sevadar = new Sevadar()
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .sevaStartDate(UPDATED_SEVA_START_DATE)
            .sevaEndDate(UPDATED_SEVA_END_DATE)
            .isValid(UPDATED_IS_VALID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return sevadar;
    }

    @BeforeEach
    public void initTest() {
        sevadar = createEntity(em);
    }

    @Test
    @Transactional
    public void createSevadar() throws Exception {
        int databaseSizeBeforeCreate = sevadarRepository.findAll().size();
        // Create the Sevadar
        restSevadarMockMvc.perform(post("/api/sevadars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sevadar)))
            .andExpect(status().isCreated());

        // Validate the Sevadar in the database
        List<Sevadar> sevadarList = sevadarRepository.findAll();
        assertThat(sevadarList).hasSize(databaseSizeBeforeCreate + 1);
        Sevadar testSevadar = sevadarList.get(sevadarList.size() - 1);
        assertThat(testSevadar.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSevadar.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSevadar.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testSevadar.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSevadar.getSevaStartDate()).isEqualTo(DEFAULT_SEVA_START_DATE);
        assertThat(testSevadar.getSevaEndDate()).isEqualTo(DEFAULT_SEVA_END_DATE);
        assertThat(testSevadar.isIsValid()).isEqualTo(DEFAULT_IS_VALID);
        assertThat(testSevadar.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testSevadar.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testSevadar.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testSevadar.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the Sevadar in Elasticsearch
        verify(mockSevadarSearchRepository, times(1)).save(testSevadar);
    }

    @Test
    @Transactional
    public void createSevadarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sevadarRepository.findAll().size();

        // Create the Sevadar with an existing ID
        sevadar.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSevadarMockMvc.perform(post("/api/sevadars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sevadar)))
            .andExpect(status().isBadRequest());

        // Validate the Sevadar in the database
        List<Sevadar> sevadarList = sevadarRepository.findAll();
        assertThat(sevadarList).hasSize(databaseSizeBeforeCreate);

        // Validate the Sevadar in Elasticsearch
        verify(mockSevadarSearchRepository, times(0)).save(sevadar);
    }


    @Test
    @Transactional
    public void getAllSevadars() throws Exception {
        // Initialize the database
        sevadarRepository.saveAndFlush(sevadar);

        // Get all the sevadarList
        restSevadarMockMvc.perform(get("/api/sevadars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sevadar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].sevaStartDate").value(hasItem(DEFAULT_SEVA_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].sevaEndDate").value(hasItem(DEFAULT_SEVA_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getSevadar() throws Exception {
        // Initialize the database
        sevadarRepository.saveAndFlush(sevadar);

        // Get the sevadar
        restSevadarMockMvc.perform(get("/api/sevadars/{id}", sevadar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sevadar.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.sevaStartDate").value(DEFAULT_SEVA_START_DATE.toString()))
            .andExpect(jsonPath("$.sevaEndDate").value(DEFAULT_SEVA_END_DATE.toString()))
            .andExpect(jsonPath("$.isValid").value(DEFAULT_IS_VALID.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingSevadar() throws Exception {
        // Get the sevadar
        restSevadarMockMvc.perform(get("/api/sevadars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSevadar() throws Exception {
        // Initialize the database
        sevadarService.save(sevadar);

        int databaseSizeBeforeUpdate = sevadarRepository.findAll().size();

        // Update the sevadar
        Sevadar updatedSevadar = sevadarRepository.findById(sevadar.getId()).get();
        // Disconnect from session so that the updates on updatedSevadar are not directly saved in db
        em.detach(updatedSevadar);
        updatedSevadar
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .sevaStartDate(UPDATED_SEVA_START_DATE)
            .sevaEndDate(UPDATED_SEVA_END_DATE)
            .isValid(UPDATED_IS_VALID)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restSevadarMockMvc.perform(put("/api/sevadars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSevadar)))
            .andExpect(status().isOk());

        // Validate the Sevadar in the database
        List<Sevadar> sevadarList = sevadarRepository.findAll();
        assertThat(sevadarList).hasSize(databaseSizeBeforeUpdate);
        Sevadar testSevadar = sevadarList.get(sevadarList.size() - 1);
        assertThat(testSevadar.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSevadar.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSevadar.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testSevadar.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSevadar.getSevaStartDate()).isEqualTo(UPDATED_SEVA_START_DATE);
        assertThat(testSevadar.getSevaEndDate()).isEqualTo(UPDATED_SEVA_END_DATE);
        assertThat(testSevadar.isIsValid()).isEqualTo(UPDATED_IS_VALID);
        assertThat(testSevadar.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testSevadar.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testSevadar.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testSevadar.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the Sevadar in Elasticsearch
        verify(mockSevadarSearchRepository, times(2)).save(testSevadar);
    }

    @Test
    @Transactional
    public void updateNonExistingSevadar() throws Exception {
        int databaseSizeBeforeUpdate = sevadarRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSevadarMockMvc.perform(put("/api/sevadars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sevadar)))
            .andExpect(status().isBadRequest());

        // Validate the Sevadar in the database
        List<Sevadar> sevadarList = sevadarRepository.findAll();
        assertThat(sevadarList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Sevadar in Elasticsearch
        verify(mockSevadarSearchRepository, times(0)).save(sevadar);
    }

    @Test
    @Transactional
    public void deleteSevadar() throws Exception {
        // Initialize the database
        sevadarService.save(sevadar);

        int databaseSizeBeforeDelete = sevadarRepository.findAll().size();

        // Delete the sevadar
        restSevadarMockMvc.perform(delete("/api/sevadars/{id}", sevadar.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sevadar> sevadarList = sevadarRepository.findAll();
        assertThat(sevadarList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Sevadar in Elasticsearch
        verify(mockSevadarSearchRepository, times(1)).deleteById(sevadar.getId());
    }

    @Test
    @Transactional
    public void searchSevadar() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sevadarService.save(sevadar);
        when(mockSevadarSearchRepository.search(queryStringQuery("id:" + sevadar.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(sevadar), PageRequest.of(0, 1), 1));

        // Search the sevadar
        restSevadarMockMvc.perform(get("/api/_search/sevadars?query=id:" + sevadar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sevadar.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].sevaStartDate").value(hasItem(DEFAULT_SEVA_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].sevaEndDate").value(hasItem(DEFAULT_SEVA_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].isValid").value(hasItem(DEFAULT_IS_VALID.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
}
