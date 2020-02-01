package com.eurofragance.bridge.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link SendToUserSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class SendToUserSearchRepositoryMockConfiguration {

    @MockBean
    private SendToUserSearchRepository mockSendToUserSearchRepository;

}
