package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.Expense;
import com.gnsg.app.repository.ExpenseRepository;
import com.gnsg.app.repository.search.ExpenseSearchRepository;
import com.gnsg.app.service.ExpenseService;

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

import com.gnsg.app.domain.enumeration.EXPTYPE;
/**
 * Integration tests for the {@link ExpenseResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ExpenseResourceIT {

    private static final EXPTYPE DEFAULT_EXP_TYPE = EXPTYPE.INVOICE;
    private static final EXPTYPE UPDATED_EXP_TYPE = EXPTYPE.RECIEPT;

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
    private ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseService expenseService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.ExpenseSearchRepositoryMockConfiguration
     */
    @Autowired
    private ExpenseSearchRepository mockExpenseSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExpenseMockMvc;

    private Expense expense;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createEntity(EntityManager em) {
        Expense expense = new Expense()
            .expType(DEFAULT_EXP_TYPE)
            .amt(DEFAULT_AMT)
            .date(DEFAULT_DATE)
            .desc(DEFAULT_DESC)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY);
        return expense;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Expense createUpdatedEntity(EntityManager em) {
        Expense expense = new Expense()
            .expType(UPDATED_EXP_TYPE)
            .amt(UPDATED_AMT)
            .date(UPDATED_DATE)
            .desc(UPDATED_DESC)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);
        return expense;
    }

    @BeforeEach
    public void initTest() {
        expense = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpense() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();
        // Create the Expense
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isCreated());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate + 1);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpType()).isEqualTo(DEFAULT_EXP_TYPE);
        assertThat(testExpense.getAmt()).isEqualTo(DEFAULT_AMT);
        assertThat(testExpense.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testExpense.getDesc()).isEqualTo(DEFAULT_DESC);
        assertThat(testExpense.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testExpense.getCreatedBy()).isEqualTo(DEFAULT_CREATED_BY);
        assertThat(testExpense.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
        assertThat(testExpense.getLastModifiedBy()).isEqualTo(DEFAULT_LAST_MODIFIED_BY);

        // Validate the Expense in Elasticsearch
        verify(mockExpenseSearchRepository, times(1)).save(testExpense);
    }

    @Test
    @Transactional
    public void createExpenseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = expenseRepository.findAll().size();

        // Create the Expense with an existing ID
        expense.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExpenseMockMvc.perform(post("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeCreate);

        // Validate the Expense in Elasticsearch
        verify(mockExpenseSearchRepository, times(0)).save(expense);
    }


    @Test
    @Transactional
    public void getAllExpenses() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get all the expenseList
        restExpenseMockMvc.perform(get("/api/expenses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expType").value(hasItem(DEFAULT_EXP_TYPE.toString())))
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
    public void getExpense() throws Exception {
        // Initialize the database
        expenseRepository.saveAndFlush(expense);

        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(expense.getId().intValue()))
            .andExpect(jsonPath("$.expType").value(DEFAULT_EXP_TYPE.toString()))
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
    public void getNonExistingExpense() throws Exception {
        // Get the expense
        restExpenseMockMvc.perform(get("/api/expenses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpense() throws Exception {
        // Initialize the database
        expenseService.save(expense);

        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // Update the expense
        Expense updatedExpense = expenseRepository.findById(expense.getId()).get();
        // Disconnect from session so that the updates on updatedExpense are not directly saved in db
        em.detach(updatedExpense);
        updatedExpense
            .expType(UPDATED_EXP_TYPE)
            .amt(UPDATED_AMT)
            .date(UPDATED_DATE)
            .desc(UPDATED_DESC)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedExpense)))
            .andExpect(status().isOk());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);
        Expense testExpense = expenseList.get(expenseList.size() - 1);
        assertThat(testExpense.getExpType()).isEqualTo(UPDATED_EXP_TYPE);
        assertThat(testExpense.getAmt()).isEqualTo(UPDATED_AMT);
        assertThat(testExpense.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testExpense.getDesc()).isEqualTo(UPDATED_DESC);
        assertThat(testExpense.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testExpense.getCreatedBy()).isEqualTo(UPDATED_CREATED_BY);
        assertThat(testExpense.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
        assertThat(testExpense.getLastModifiedBy()).isEqualTo(UPDATED_LAST_MODIFIED_BY);

        // Validate the Expense in Elasticsearch
        verify(mockExpenseSearchRepository, times(2)).save(testExpense);
    }

    @Test
    @Transactional
    public void updateNonExistingExpense() throws Exception {
        int databaseSizeBeforeUpdate = expenseRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExpenseMockMvc.perform(put("/api/expenses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(expense)))
            .andExpect(status().isBadRequest());

        // Validate the Expense in the database
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Expense in Elasticsearch
        verify(mockExpenseSearchRepository, times(0)).save(expense);
    }

    @Test
    @Transactional
    public void deleteExpense() throws Exception {
        // Initialize the database
        expenseService.save(expense);

        int databaseSizeBeforeDelete = expenseRepository.findAll().size();

        // Delete the expense
        restExpenseMockMvc.perform(delete("/api/expenses/{id}", expense.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Expense> expenseList = expenseRepository.findAll();
        assertThat(expenseList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Expense in Elasticsearch
        verify(mockExpenseSearchRepository, times(1)).deleteById(expense.getId());
    }

    @Test
    @Transactional
    public void searchExpense() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        expenseService.save(expense);
        when(mockExpenseSearchRepository.search(queryStringQuery("id:" + expense.getId())))
            .thenReturn(Collections.singletonList(expense));

        // Search the expense
        restExpenseMockMvc.perform(get("/api/_search/expenses?query=id:" + expense.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(expense.getId().intValue())))
            .andExpect(jsonPath("$.[*].expType").value(hasItem(DEFAULT_EXP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].amt").value(hasItem(DEFAULT_AMT.doubleValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].desc").value(hasItem(DEFAULT_DESC)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)));
    }
}
