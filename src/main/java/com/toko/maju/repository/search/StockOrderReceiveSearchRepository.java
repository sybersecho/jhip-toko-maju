package com.toko.maju.repository.search;

import com.toko.maju.domain.StockOrderReceive;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockOrderReceive entity.
 */
public interface StockOrderReceiveSearchRepository extends ElasticsearchRepository<StockOrderReceive, Long> {
}
