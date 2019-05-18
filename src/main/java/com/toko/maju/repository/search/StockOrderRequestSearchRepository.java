package com.toko.maju.repository.search;

import com.toko.maju.domain.StockOrderRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockOrderRequest entity.
 */
public interface StockOrderRequestSearchRepository extends ElasticsearchRepository<StockOrderRequest, Long> {
}
