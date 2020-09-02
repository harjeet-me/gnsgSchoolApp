package com.gnsg.app.service.impl;

import com.gnsg.app.service.StudentService;
import com.gnsg.app.domain.Student;
import com.gnsg.app.repository.StudentRepository;
import com.gnsg.app.repository.search.StudentSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Student}.
 */
@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final Logger log = LoggerFactory.getLogger(StudentServiceImpl.class);

    private final StudentRepository studentRepository;

    private final StudentSearchRepository studentSearchRepository;

    public StudentServiceImpl(StudentRepository studentRepository, StudentSearchRepository studentSearchRepository) {
        this.studentRepository = studentRepository;
        this.studentSearchRepository = studentSearchRepository;
    }

    /**
     * Save a student.
     *
     * @param student the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Student save(Student student) {
        log.debug("Request to save Student : {}", student);
        Student result = studentRepository.save(student);
        studentSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the students.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Student> findAll(Pageable pageable) {
        log.debug("Request to get all Students");
        return studentRepository.findAll(pageable);
    }


    /**
     * Get all the students with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Student> findAllWithEagerRelationships(Pageable pageable) {
        return studentRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one student by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Student> findOne(Long id) {
        log.debug("Request to get Student : {}", id);
        return studentRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the student by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Student : {}", id);

        studentRepository.deleteById(id);
        studentSearchRepository.deleteById(id);
    }

    /**
     * Search for the student corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Student> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Students for query {}", query);
        return studentSearchRepository.search(queryStringQuery(query), pageable);    }
}
