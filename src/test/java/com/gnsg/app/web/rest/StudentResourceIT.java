package com.gnsg.app.web.rest;

import com.gnsg.app.GnsgSchoolApp;
import com.gnsg.app.domain.Student;
import com.gnsg.app.repository.StudentRepository;
import com.gnsg.app.repository.search.StudentSearchRepository;
import com.gnsg.app.service.StudentService;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link StudentResource} REST controller.
 */
@SpringBootTest(classes = GnsgSchoolApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class StudentResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE_NUMBER = 1L;
    private static final Long UPDATED_PHONE_NUMBER = 2L;

    private static final String DEFAULT_TEACHER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TEACHER_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private StudentRepository studentRepository;

    @Mock
    private StudentRepository studentRepositoryMock;

    @Mock
    private StudentService studentServiceMock;

    @Autowired
    private StudentService studentService;

    /**
     * This repository is mocked in the com.gnsg.app.repository.search test package.
     *
     * @see com.gnsg.app.repository.search.StudentSearchRepositoryMockConfiguration
     */
    @Autowired
    private StudentSearchRepository mockStudentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStudentMockMvc;

    private Student student;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .fullName(DEFAULT_FULL_NAME)
            .parentName(DEFAULT_PARENT_NAME)
            .address(DEFAULT_ADDRESS)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .teacherName(DEFAULT_TEACHER_NAME)
            .active(DEFAULT_ACTIVE);
        return student;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Student createUpdatedEntity(EntityManager em) {
        Student student = new Student()
            .fullName(UPDATED_FULL_NAME)
            .parentName(UPDATED_PARENT_NAME)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .teacherName(UPDATED_TEACHER_NAME)
            .active(UPDATED_ACTIVE);
        return student;
    }

    @BeforeEach
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();
        // Create the Student
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testStudent.getParentName()).isEqualTo(DEFAULT_PARENT_NAME);
        assertThat(testStudent.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStudent.getTeacherName()).isEqualTo(DEFAULT_TEACHER_NAME);
        assertThat(testStudent.isActive()).isEqualTo(DEFAULT_ACTIVE);

        // Validate the Student in Elasticsearch
        verify(mockStudentSearchRepository, times(1)).save(testStudent);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);

        // Validate the Student in Elasticsearch
        verify(mockStudentSearchRepository, times(0)).save(student);
    }


    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].parentName").value(hasItem(DEFAULT_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].teacherName").value(hasItem(DEFAULT_TEACHER_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllStudentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(studentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentMockMvc.perform(get("/api/students?eagerload=true"))
            .andExpect(status().isOk());

        verify(studentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllStudentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(studentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restStudentMockMvc.perform(get("/api/students?eagerload=true"))
            .andExpect(status().isOk());

        verify(studentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.parentName").value(DEFAULT_PARENT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.intValue()))
            .andExpect(jsonPath("$.teacherName").value(DEFAULT_TEACHER_NAME))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }
    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .fullName(UPDATED_FULL_NAME)
            .parentName(UPDATED_PARENT_NAME)
            .address(UPDATED_ADDRESS)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .teacherName(UPDATED_TEACHER_NAME)
            .active(UPDATED_ACTIVE);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudent)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testStudent.getParentName()).isEqualTo(UPDATED_PARENT_NAME);
        assertThat(testStudent.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getTeacherName()).isEqualTo(UPDATED_TEACHER_NAME);
        assertThat(testStudent.isActive()).isEqualTo(UPDATED_ACTIVE);

        // Validate the Student in Elasticsearch
        verify(mockStudentSearchRepository, times(2)).save(testStudent);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Student in Elasticsearch
        verify(mockStudentSearchRepository, times(0)).save(student);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Delete the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Student in Elasticsearch
        verify(mockStudentSearchRepository, times(1)).deleteById(student.getId());
    }

    @Test
    @Transactional
    public void searchStudent() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        studentService.save(student);
        when(mockStudentSearchRepository.search(queryStringQuery("id:" + student.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(student), PageRequest.of(0, 1), 1));

        // Search the student
        restStudentMockMvc.perform(get("/api/_search/students?query=id:" + student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].parentName").value(hasItem(DEFAULT_PARENT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.intValue())))
            .andExpect(jsonPath("$.[*].teacherName").value(hasItem(DEFAULT_TEACHER_NAME)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
}
