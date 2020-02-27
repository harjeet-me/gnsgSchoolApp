package com.gnsg.app.repository.search;

import com.gnsg.app.domain.AppliedCharge;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link AppliedCharge} entity.
 */
public interface AppliedChargeSearchRepository extends ElasticsearchRepository<AppliedCharge, Long> {
}
