package com.gnsg.app.repository.search;

import com.gnsg.app.domain.PRoul;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link PRoul} entity.
 */
public interface PRoulSearchRepository extends ElasticsearchRepository<PRoul, Long> {
}
