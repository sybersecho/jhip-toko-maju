package com.toko.maju.repository.search;

import com.toko.maju.domain.StockOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the StockOrder entity.
 */
public interface StockOrderSearchRepository extends ElasticsearchRepository<StockOrder, Long> {
}
