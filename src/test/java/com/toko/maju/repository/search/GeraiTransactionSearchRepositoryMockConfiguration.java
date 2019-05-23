package com.toko.maju.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of GeraiTransactionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class GeraiTransactionSearchRepositoryMockConfiguration {

    @MockBean
    private GeraiTransactionSearchRepository mockGeraiTransactionSearchRepository;

}
