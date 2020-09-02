package com.gnsg.app.repository.search;

import com.gnsg.app.domain.Sevadar;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Sevadar} entity.
 */
public interface SevadarSearchRepository extends ElasticsearchRepository<Sevadar, Long> {
}
