package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.ASPath;
import com.gnsg.app.repository.ASPathRepository;
import com.gnsg.app.repository.search.ASPathSearchRepository;
import com.gnsg.app.service.ASPathService;

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

import com.gnsg.app.domain.enumeration.PATHTYPE;
import com.gnsg.app.domain.enumeration.EventStatus;
/**
 * Integration tests for the {@link ASPathResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ASPathResourceIT {

    private static final PATHTYPE DEFAULT_PROGRAM = PATHTYPE.AKHAND_PATH;
    private static final PATHTYPE UPDATED_PROGRAM = PATHTYPE.SEHAJ_PATH;

    private static final String DEFAULT_FAMILY = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Instant DEFAULT_BOOKING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BOOKING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESC = "AAAAAAAAAA";
    private static final String UPDATED_DESC = "BBBBBBBBBB";

    private static final EventStatus DEFAULT_STATUS = EventStatus.BOOKED;
    private static final EventStatus UPDATED_STATUS = EventStatus.COMPLETED;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    @Autowired
    private ASPathRepository aSPathRepository;

    @Autowired
    private ASPathService aSPathService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.ASPathSearchRepositoryMockConfiguration
     */
    @Autowired
    private ASPathSearchRepository mockASPathSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restASPathMockMvc;

    private ASPath aSPath;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ASPath createEntity(EntityManager em) {
        ASPath aSPath = new ASPath()
            .program(DEFAULT_PROGRAM)
            .family(DEFAULT_FAMILY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .remark(DEFAULT_REMARK)
            .bookingDate(DEFAULT_BOOKING_DATE)
            .desc(DEFAULT_DESC)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return aSPath;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ASPath createUpdatedEntity(EntityManager em) {
        ASPath aSPath = new ASPath()
            .program(UPDATED_PROGRAM)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .remark(UPDATED_REMARK)
            .bookingDate(UPDATED_BOOKING_DATE)
            .desc(UPDATED_DESC)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return aSPath;
    }

    @BeforeEach
    public void initTest() {
        aSPath = createEntity(em);
    }

    @Test
    @Transactional
    public void createASPath() throws Exception {
        int databaseSizeBeforeCreate = aSPathRepository.findAll().size();
        // Create the ASPath
        restASPathMockMvc.perform(post("/api/as-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aSPath)))
            .andExpect(status().isCreated());

        // Validate the ASPath in the database
        List<ASPath> aSPathList = aSPathRepository.findAll();
        assertThat(aSPathList).hasSize(databaseSizeBeforeCreate + 1);
        ASPath testASPath = aSPathList.get(aSPathList.size() - 1);
        assertThat(testASPath.getProgram()).isEqualTo(DEFAULT_PROGRAM);
        assertThat(testASPath.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testASPath.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testASPath.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testASPath.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testASPath.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testASPath.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testASPath.getBookingDate()).isEqualTo(DEFAULT_BOOKING_DATE);
        assertThat(testASPath.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testASPath.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testASPath.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testASPath.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testASPath.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testASPath.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the ASPath in Elasticsearch
        verify(mockASPathSearchRepository, times(1)).save(testASPath);
    }

    @Test
    @Transactional
    public void createASPathWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = aSPathRepository.findAll().size();

        // Create the ASPath with an existing ID
        aSPath.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restASPathMockMvc.perform(post("/api/as-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aSPath)))
            .andExpect(status().isBadRequest());

        // Validate the ASPath in the database
        List<ASPath> aSPathList = aSPathRepository.findAll();
        assertThat(aSPathList).hasSize(databaseSizeBeforeCreate);

        // Validate the ASPath in Elasticsearch
        verify(mockASPathSearchRepository, times(0)).save(aSPath);
    }


    @Test
    @Transactional
    public void getAllASPaths() throws Exception {
        // Initialize the database
        aSPathRepository.saveAndFlush(aSPath);

        // Get all the aSPathList
        restASPathMockMvc.perform(get("/api/as-paths?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aSPath.getId().intValue())))
            .andExpect(jsonPath("$.[*].program").value(hasItem(DEFAULT_PROGRAM.toString())))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].bookingDate").value(hasItem(DEFAULT_BOOKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getASPath() throws Exception {
        // Initialize the database
        aSPathRepository.saveAndFlush(aSPath);

        // Get the aSPath
        restASPathMockMvc.perform(get("/api/as-paths/{id}", aSPath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aSPath.getId().intValue()))
            .andExpect(jsonPath("$.program").value(DEFAULT_PROGRAM.toString()))
            .andExpect(jsonPath("$.family").value(DEFAULT_FAMILY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.bookingDate").value(DEFAULT_BOOKING_DATE.toString()))
            .andExpect(jsonPath("$.desc").value(DEFAULT_DESC))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingASPath() throws Exception {
        // Get the aSPath
        restASPathMockMvc.perform(get("/api/as-paths/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateASPath() throws Exception {
        // Initialize the database
        aSPathService.save(aSPath);

        int databaseSizeBeforeUpdate = aSPathRepository.findAll().size();

        // Update the aSPath
        ASPath updatedASPath = aSPathRepository.findById(aSPath.getId()).get();
        // Disconnect from session so that the updates on updatedASPath are not directly saved in db
        em.detach(updatedASPath);
        updatedASPath
            .program(UPDATED_PROGRAM)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .remark(UPDATED_REMARK)
            .bookingDate(UPDATED_BOOKING_DATE)
            .desc(UPDATED_DESC)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restASPathMockMvc.perform(put("/api/as-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedASPath)))
            .andExpect(status().isOk());

        // Validate the ASPath in the database
        List<ASPath> aSPathList = aSPathRepository.findAll();
        assertThat(aSPathList).hasSize(databaseSizeBeforeUpdate);
        ASPath testASPath = aSPathList.get(aSPathList.size() - 1);
        assertThat(testASPath.getProgram()).isEqualTo(UPDATED_PROGRAM);
        assertThat(testASPath.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testASPath.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testASPath.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testASPath.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testASPath.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testASPath.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testASPath.getBookingDate()).isEqualTo(UPDATED_BOOKING_DATE);
        assertThat(testASPath.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testASPath.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testASPath.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testASPath.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testASPath.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testASPath.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the ASPath in Elasticsearch
        verify(mockASPathSearchRepository, times(2)).save(testASPath);
    }

    @Test
    @Transactional
    public void updateNonExistingASPath() throws Exception {
        int databaseSizeBeforeUpdate = aSPathRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restASPathMockMvc.perform(put("/api/as-paths")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(aSPath)))
            .andExpect(status().isBadRequest());

        // Validate the ASPath in the database
        List<ASPath> aSPathList = aSPathRepository.findAll();
        assertThat(aSPathList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ASPath in Elasticsearch
        verify(mockASPathSearchRepository, times(0)).save(aSPath);
    }

    @Test
    @Transactional
    public void deleteASPath() throws Exception {
        // Initialize the database
        aSPathService.save(aSPath);

        int databaseSizeBeforeDelete = aSPathRepository.findAll().size();

        // Delete the aSPath
        restASPathMockMvc.perform(delete("/api/as-paths/{id}", aSPath.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ASPath> aSPathList = aSPathRepository.findAll();
        assertThat(aSPathList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ASPath in Elasticsearch
        verify(mockASPathSearchRepository, times(1)).deleteById(aSPath.getId());
    }

    @Test
    @Transactional
    public void searchASPath() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        aSPathService.save(aSPath);
        when(mockASPathSearchRepository.search(queryStringQuery("id:" + aSPath.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(aSPath), PageRequest.of(0, 1), 1));

        // Search the aSPath
        restASPathMockMvc.perform(get("/api/_search/as-paths?query=id:" + aSPath.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aSPath.getId().intValue())))
            .andExpect(jsonPath("$.[*].program").value(hasItem(DEFAULT_PROGRAM.toString())))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].bookingDate").value(hasItem(DEFAULT_BOOKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
}
