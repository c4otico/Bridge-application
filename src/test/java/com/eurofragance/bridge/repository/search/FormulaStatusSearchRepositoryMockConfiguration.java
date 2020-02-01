package com.eurofragance.bridge.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link FormulaStatusSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class FormulaStatusSearchRepositoryMockConfiguration {

    @MockBean
    private FormulaStatusSearchRepository mockFormulaStatusSearchRepository;

}
