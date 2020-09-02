package com.gnsg.app.repository.search;

import com.gnsg.app.domain.Student;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Student} entity.
 */
public interface StudentSearchRepository extends ElasticsearchRepository<Student, Long> {
}
