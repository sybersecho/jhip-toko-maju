package com.toko.maju.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of CancelTransactionSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CancelTransactionSearchRepositoryMockConfiguration {

    @MockBean
    private CancelTransactionSearchRepository mockCancelTransactionSearchRepository;

}
