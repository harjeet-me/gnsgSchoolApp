package com.gnsg.app.repository.search;

import com.gnsg.app.domain.Expense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Expense} entity.
 */
public interface ExpenseSearchRepository extends ElasticsearchRepository<Expense, Long> {
}
