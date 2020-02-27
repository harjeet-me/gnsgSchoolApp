package com.gnsg.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ChargeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ChargeSearchRepositoryMockConfiguration {

    @MockBean
    private ChargeSearchRepository mockChargeSearchRepository;

}
