package com.toko.maju.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of StockOrderProcessSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class StockOrderProcessSearchRepositoryMockConfiguration {

    @MockBean
    private StockOrderProcessSearchRepository mockStockOrderProcessSearchRepository;

}
