package com.gnsg.app.repository.search;

import com.gnsg.app.domain.Charge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Charge} entity.
 */
public interface ChargeSearchRepository extends ElasticsearchRepository<Charge, Long> {
}
