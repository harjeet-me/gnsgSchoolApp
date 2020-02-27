package com.gnsg.app.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link AppliedChargeSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class AppliedChargeSearchRepositoryMockConfiguration {

    @MockBean
    private AppliedChargeSearchRepository mockAppliedChargeSearchRepository;

}
