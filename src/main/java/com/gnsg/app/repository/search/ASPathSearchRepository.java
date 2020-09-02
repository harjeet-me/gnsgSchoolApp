package com.gnsg.app.repository.search;

import com.gnsg.app.domain.ASPath;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link ASPath} entity.
 */
public interface ASPathSearchRepository extends ElasticsearchRepository<ASPath, Long> {
}
