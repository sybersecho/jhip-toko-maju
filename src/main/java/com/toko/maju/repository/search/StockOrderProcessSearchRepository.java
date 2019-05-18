package com.toko.maju.repository.search;

import com.toko.maju.domain.StockOrderProcess;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockOrderProcess entity.
 */
public interface StockOrderProcessSearchRepository extends ElasticsearchRepository<StockOrderProcess, Long> {
}
