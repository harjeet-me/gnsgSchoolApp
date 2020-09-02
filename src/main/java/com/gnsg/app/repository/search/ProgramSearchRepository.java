package com.gnsg.app.repository.search;

import com.gnsg.app.domain.Program;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Program} entity.
 */
public interface ProgramSearchRepository extends ElasticsearchRepository<Program, Long> {
}
