package com.gnsg.app.repository.search;

import com.gnsg.app.domain.Revenue;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Revenue} entity.
 */
public interface RevenueSearchRepository extends ElasticsearchRepository<Revenue, Long> {
}
