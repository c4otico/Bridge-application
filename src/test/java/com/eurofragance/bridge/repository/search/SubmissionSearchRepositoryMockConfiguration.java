package com.eurofragance.bridge.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SubmissionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SubmissionSearchRepositoryMockConfiguration {

    @MockBean
    private SubmissionSearchRepository mockSubmissionSearchRepository;

}
