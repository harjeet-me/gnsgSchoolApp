package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.Revenue;
import com.gnsg.app.repository.RevenueRepository;
import com.gnsg.app.repository.search.RevenueSearchRepository;
import com.gnsg.app.service.RevenueService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gnsg.app.domain.enumeration.REVTYPE;
/**
 * Integration tests for the {@link RevenueResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RevenueResourceIT {

    private static final REVTYPE DEFAULT_REV_TYPE = REVTYPE.PROGRAM;
    private static final REVTYPE UPDATED_REV_TYPE = REVTYPE.GOLAK;

    private static final Double DEFAULT_AMT = 1D;
    private static final Double UPDATED_AMT = 2D;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private RevenueRepository revenueRepository;

    @Autowired
    private RevenueService revenueService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.RevenueSearchRepositoryMockConfiguration
     */
    @Autowired
    private RevenueSearchRepository mockRevenueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRevenueMockMvc;

    private Revenue revenue;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revenue createEntity(EntityManager em) {
        Revenue revenue = new Revenue()
            .revType(DEFAULT_REV_TYPE)
            .amt(DEFAULT_AMT)
            .date(DEFAULT_DATE)
            .desc(DEFAULT_DESC)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return revenue;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Revenue createUpdatedEntity(EntityManager em) {
        Revenue revenue = new Revenue()
            .revType(UPDATED_REV_TYPE)
            .amt(UPDATED_AMT)
            .date(UPDATED_DATE)
            .desc(UPDATED_DESC)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return revenue;
    }

    @BeforeEach
    public void initTest() {
        revenue = createEntity(em);
    }

    @Test
    @Transactional
    public void createRevenue() throws Exception {
        int databaseSizeBeforeCreate = revenueRepository.findAll().size();
        // Create the Revenue
        restRevenueMockMvc.perform(post("/api/revenues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revenue)))
            .andExpect(status().isCreated());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeCreate + 1);
        Revenue testRevenue = revenueList.get(revenueList.size() - 1);
        assertThat(testRevenue.getRevType()).isEqualTo(DEFAULT_REV_TYPE);
        assertThat(testRevenue.getAmt()).isEqualTo(DEFAULT_AMT);
        assertThat(testRevenue.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRevenue.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testRevenue.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testRevenue.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testRevenue.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testRevenue.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the Revenue in Elasticsearch
        verify(mockRevenueSearchRepository, times(1)).save(testRevenue);
    }

    @Test
    @Transactional
    public void createRevenueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = revenueRepository.findAll().size();

        // Create the Revenue with an existing ID
        revenue.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRevenueMockMvc.perform(post("/api/revenues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revenue)))
            .andExpect(status().isBadRequest());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeCreate);

        // Validate the Revenue in Elasticsearch
        verify(mockRevenueSearchRepository, times(0)).save(revenue);
    }


    @Test
    @Transactional
    public void getAllRevenues() throws Exception {
        // Initialize the database
        revenueRepository.saveAndFlush(revenue);

        // Get all the revenueList
        restRevenueMockMvc.perform(get("/api/revenues?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revenue.getId().intValue())))
            .andExpect(jsonPath("$.[*].revType").value(hasItem(DEFAULT_REV_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amt").value(hasItem(DEFAULT_AMT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getRevenue() throws Exception {
        // Initialize the database
        revenueRepository.saveAndFlush(revenue);

        // Get the revenue
        restRevenueMockMvc.perform(get("/api/revenues/{id}", revenue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(revenue.getId().intValue()))
            .andExpect(jsonPath("$.revType").value(DEFAULT_REV_TYPE.toString()))
            .andExpect(jsonPath("$.amt").value(DEFAULT_AMT.doubleValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingRevenue() throws Exception {
        // Get the revenue
        restRevenueMockMvc.perform(get("/api/revenues/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRevenue() throws Exception {
        // Initialize the database
        revenueService.save(revenue);

        int databaseSizeBeforeUpdate = revenueRepository.findAll().size();

        // Update the revenue
        Revenue updatedRevenue = revenueRepository.findById(revenue.getId()).get();
        // Disconnect from session so that the updates on updatedRevenue are not directly saved in db
        em.detach(updatedRevenue);
        updatedRevenue
            .revType(UPDATED_REV_TYPE)
            .amt(UPDATED_AMT)
            .date(UPDATED_DATE)
            .desc(UPDATED_DESC)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restRevenueMockMvc.perform(put("/api/revenues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedRevenue)))
            .andExpect(status().isOk());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeUpdate);
        Revenue testRevenue = revenueList.get(revenueList.size() - 1);
        assertThat(testRevenue.getRevType()).isEqualTo(UPDATED_REV_TYPE);
        assertThat(testRevenue.getAmt()).isEqualTo(UPDATED_AMT);
        assertThat(testRevenue.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRevenue.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testRevenue.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testRevenue.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testRevenue.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testRevenue.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the Revenue in Elasticsearch
        verify(mockRevenueSearchRepository, times(2)).save(testRevenue);
    }

    @Test
    @Transactional
    public void updateNonExistingRevenue() throws Exception {
        int databaseSizeBeforeUpdate = revenueRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRevenueMockMvc.perform(put("/api/revenues")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(revenue)))
            .andExpect(status().isBadRequest());

        // Validate the Revenue in the database
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Revenue in Elasticsearch
        verify(mockRevenueSearchRepository, times(0)).save(revenue);
    }

    @Test
    @Transactional
    public void deleteRevenue() throws Exception {
        // Initialize the database
        revenueService.save(revenue);

        int databaseSizeBeforeDelete = revenueRepository.findAll().size();

        // Delete the revenue
        restRevenueMockMvc.perform(delete("/api/revenues/{id}", revenue.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Revenue> revenueList = revenueRepository.findAll();
        assertThat(revenueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Revenue in Elasticsearch
        verify(mockRevenueSearchRepository, times(1)).deleteById(revenue.getId());
    }

    @Test
    @Transactional
    public void searchRevenue() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        revenueService.save(revenue);
        when(mockRevenueSearchRepository.search(queryStringQuery("id:" + revenue.getId())))
            .thenReturn(Collections.singletonList(revenue));

        // Search the revenue
        restRevenueMockMvc.perform(get("/api/_search/revenues?query=id:" + revenue.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(revenue.getId().intValue())))
            .andExpect(jsonPath("$.[*].revType").value(hasItem(DEFAULT_REV_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amt").value(hasItem(DEFAULT_AMT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
}
