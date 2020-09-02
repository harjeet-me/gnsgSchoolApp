package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.Program;
import com.gnsg.app.repository.ProgramRepository;
import com.gnsg.app.repository.search.ProgramSearchRepository;
import com.gnsg.app.service.ProgramService;

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
import java.math.BigDecimal;
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

import com.gnsg.app.domain.enumeration.EVENTTYPE;
import com.gnsg.app.domain.enumeration.EVENTLOCATION;
import com.gnsg.app.domain.enumeration.LANGARMENU;
import com.gnsg.app.domain.enumeration.EventStatus;
/**
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ProgramResourceIT {

    private static final EVENTTYPE DEFAULT_PROGRAM_TYPE = EVENTTYPE.SUKHMANI_SAHIB;
    private static final EVENTTYPE UPDATED_PROGRAM_TYPE = EVENTTYPE.SUKHMANI_SAHIB_AT_HOME;

    private static final EVENTLOCATION DEFAULT_LOCATION = EVENTLOCATION.HALL_2;
    private static final EVENTLOCATION UPDATED_LOCATION = EVENTLOCATION.HALL_3;

    private static final Instant DEFAULT_ETIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ETIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_FAMILY = "AAAAAAAAAA";
    private static final String UPDATED_FAMILY = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WITH_LANGAR = false;
    private static final Boolean UPDATED_WITH_LANGAR = true;

    private static final LANGARMENU DEFAULT_LANGAR_MENU = LANGARMENU.SIMPLE_JALEBI_SHAHIPANEER;
    private static final LANGARMENU UPDATED_LANGAR_MENU = LANGARMENU.SIMPLE_JALEBI_MATARPANEER;

    private static final Instant DEFAULT_LANGAR_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LANGAR_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_DUE_AMT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DUE_AMT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PAID_AMT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PAID_AMT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BAL_AMT = new BigDecimal(1);
    private static final BigDecimal UPDATED_BAL_AMT = new BigDecimal(2);

    private static final Long DEFAULT_RECIEPT_NUMBER = 1L;
    private static final Long UPDATED_RECIEPT_NUMBER = 2L;

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final Instant DEFAULT_BOOKING_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BOOKING_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

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
    private ProgramRepository programRepository;

    @Autowired
    private ProgramService programService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.ProgramSearchRepositoryMockConfiguration
     */
    @Autowired
    private ProgramSearchRepository mockProgramSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgramMockMvc;

    private Program program;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .programType(DEFAULT_PROGRAM_TYPE)
            .location(DEFAULT_LOCATION)
            .etime(DEFAULT_ETIME)
            .family(DEFAULT_FAMILY)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .address(DEFAULT_ADDRESS)
            .withLangar(DEFAULT_WITH_LANGAR)
            .langarMenu(DEFAULT_LANGAR_MENU)
            .langarTime(DEFAULT_LANGAR_TIME)
            .dueAmt(DEFAULT_DUE_AMT)
            .paidAmt(DEFAULT_PAID_AMT)
            .balAmt(DEFAULT_BAL_AMT)
            .recieptNumber(DEFAULT_RECIEPT_NUMBER)
            .remark(DEFAULT_REMARK)
            .bookingDate(DEFAULT_BOOKING_DATE)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return program;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity(EntityManager em) {
        Program program = new Program()
            .programType(UPDATED_PROGRAM_TYPE)
            .location(UPDATED_LOCATION)
            .etime(UPDATED_ETIME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .withLangar(UPDATED_WITH_LANGAR)
            .langarMenu(UPDATED_LANGAR_MENU)
            .langarTime(UPDATED_LANGAR_TIME)
            .dueAmt(UPDATED_DUE_AMT)
            .paidAmt(UPDATED_PAID_AMT)
            .balAmt(UPDATED_BAL_AMT)
            .recieptNumber(UPDATED_RECIEPT_NUMBER)
            .remark(UPDATED_REMARK)
            .bookingDate(UPDATED_BOOKING_DATE)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return program;
    }

    @BeforeEach
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();
        // Create the Program
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramType()).isEqualTo(DEFAULT_PROGRAM_TYPE);
        assertThat(testProgram.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testProgram.getEtime()).isEqualTo(DEFAULT_ETIME);
        assertThat(testProgram.getFamily()).isEqualTo(DEFAULT_FAMILY);
        assertThat(testProgram.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testProgram.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testProgram.isWithLangar()).isEqualTo(DEFAULT_WITH_LANGAR);
        assertThat(testProgram.getLangarMenu()).isEqualTo(DEFAULT_LANGAR_MENU);
        assertThat(testProgram.getLangarTime()).isEqualTo(DEFAULT_LANGAR_TIME);
        assertThat(testProgram.getDueAmt()).isEqualTo(DEFAULT_DUE_AMT);
        assertThat(testProgram.getPaidAmt()).isEqualTo(DEFAULT_PAID_AMT);
        assertThat(testProgram.getBalAmt()).isEqualTo(DEFAULT_BAL_AMT);
        assertThat(testProgram.getRecieptNumber()).isEqualTo(DEFAULT_RECIEPT_NUMBER);
        assertThat(testProgram.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testProgram.getBookingDate()).isEqualTo(DEFAULT_BOOKING_DATE);
        assertThat(testProgram.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testProgram.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProgram.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testProgram.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testProgram.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(1)).save(testProgram);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(0)).save(program);
    }


    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programType").value(hasItem(DEFAULT_PROGRAM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].etime").value(hasItem(DEFAULT_ETIME.toString())))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].withLangar").value(hasItem(DEFAULT_WITH_LANGAR.booleanValue())))
            .andExpect(jsonPath("$.[*].langarMenu").value(hasItem(DEFAULT_LANGAR_MENU.toString())))
            .andExpect(jsonPath("$.[*].langarTime").value(hasItem(DEFAULT_LANGAR_TIME.toString())))
            .andExpect(jsonPath("$.[*].dueAmt").value(hasItem(DEFAULT_DUE_AMT.intValue())))
            .andExpect(jsonPath("$.[*].paidAmt").value(hasItem(DEFAULT_PAID_AMT.intValue())))
            .andExpect(jsonPath("$.[*].balAmt").value(hasItem(DEFAULT_BAL_AMT.intValue())))
            .andExpect(jsonPath("$.[*].recieptNumber").value(hasItem(DEFAULT_RECIEPT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].bookingDate").value(hasItem(DEFAULT_BOOKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
    
    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.programType").value(DEFAULT_PROGRAM_TYPE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.etime").value(DEFAULT_ETIME.toString()))
            .andExpect(jsonPath("$.family").value(DEFAULT_FAMILY))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.withLangar").value(DEFAULT_WITH_LANGAR.booleanValue()))
            .andExpect(jsonPath("$.langarMenu").value(DEFAULT_LANGAR_MENU.toString()))
            .andExpect(jsonPath("$.langarTime").value(DEFAULT_LANGAR_TIME.toString()))
            .andExpect(jsonPath("$.dueAmt").value(DEFAULT_DUE_AMT.intValue()))
            .andExpect(jsonPath("$.paidAmt").value(DEFAULT_PAID_AMT.intValue()))
            .andExpect(jsonPath("$.balAmt").value(DEFAULT_BAL_AMT.intValue()))
            .andExpect(jsonPath("$.recieptNumber").value(DEFAULT_RECIEPT_NUMBER.intValue()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK))
            .andExpect(jsonPath("$.bookingDate").value(DEFAULT_BOOKING_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY));
    }
    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programService.save(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .programType(UPDATED_PROGRAM_TYPE)
            .location(UPDATED_LOCATION)
            .etime(UPDATED_ETIME)
            .family(UPDATED_FAMILY)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .address(UPDATED_ADDRESS)
            .withLangar(UPDATED_WITH_LANGAR)
            .langarMenu(UPDATED_LANGAR_MENU)
            .langarTime(UPDATED_LANGAR_TIME)
            .dueAmt(UPDATED_DUE_AMT)
            .paidAmt(UPDATED_PAID_AMT)
            .balAmt(UPDATED_BAL_AMT)
            .recieptNumber(UPDATED_RECIEPT_NUMBER)
            .remark(UPDATED_REMARK)
            .bookingDate(UPDATED_BOOKING_DATE)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgram)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getProgramType()).isEqualTo(UPDATED_PROGRAM_TYPE);
        assertThat(testProgram.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testProgram.getEtime()).isEqualTo(UPDATED_ETIME);
        assertThat(testProgram.getFamily()).isEqualTo(UPDATED_FAMILY);
        assertThat(testProgram.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testProgram.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testProgram.isWithLangar()).isEqualTo(UPDATED_WITH_LANGAR);
        assertThat(testProgram.getLangarMenu()).isEqualTo(UPDATED_LANGAR_MENU);
        assertThat(testProgram.getLangarTime()).isEqualTo(UPDATED_LANGAR_TIME);
        assertThat(testProgram.getDueAmt()).isEqualTo(UPDATED_DUE_AMT);
        assertThat(testProgram.getPaidAmt()).isEqualTo(UPDATED_PAID_AMT);
        assertThat(testProgram.getBalAmt()).isEqualTo(UPDATED_BAL_AMT);
        assertThat(testProgram.getRecieptNumber()).isEqualTo(UPDATED_RECIEPT_NUMBER);
        assertThat(testProgram.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testProgram.getBookingDate()).isEqualTo(UPDATED_BOOKING_DATE);
        assertThat(testProgram.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testProgram.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProgram.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testProgram.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testProgram.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(2)).save(testProgram);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(0)).save(program);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programService.save(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Delete the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Program in Elasticsearch
        verify(mockProgramSearchRepository, times(1)).deleteById(program.getId());
    }

    @Test
    @Transactional
    public void searchProgram() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        programService.save(program);
        when(mockProgramSearchRepository.search(queryStringQuery("id:" + program.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(program), PageRequest.of(0, 1), 1));

        // Search the program
        restProgramMockMvc.perform(get("/api/_search/programs?query=id:" + program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].programType").value(hasItem(DEFAULT_PROGRAM_TYPE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].etime").value(hasItem(DEFAULT_ETIME.toString())))
            .andExpect(jsonPath("$.[*].family").value(hasItem(DEFAULT_FAMILY)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].withLangar").value(hasItem(DEFAULT_WITH_LANGAR.booleanValue())))
            .andExpect(jsonPath("$.[*].langarMenu").value(hasItem(DEFAULT_LANGAR_MENU.toString())))
            .andExpect(jsonPath("$.[*].langarTime").value(hasItem(DEFAULT_LANGAR_TIME.toString())))
            .andExpect(jsonPath("$.[*].dueAmt").value(hasItem(DEFAULT_DUE_AMT.intValue())))
            .andExpect(jsonPath("$.[*].paidAmt").value(hasItem(DEFAULT_PAID_AMT.intValue())))
            .andExpect(jsonPath("$.[*].balAmt").value(hasItem(DEFAULT_BAL_AMT.intValue())))
            .andExpect(jsonPath("$.[*].recieptNumber").value(hasItem(DEFAULT_RECIEPT_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK)))
            .andExpect(jsonPath("$.[*].bookingDate").value(hasItem(DEFAULT_BOOKING_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
}
