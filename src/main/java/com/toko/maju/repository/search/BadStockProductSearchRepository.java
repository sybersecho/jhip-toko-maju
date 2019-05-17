package com.toko.maju.repository.search;

import com.toko.maju.domain.BadStockProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the BadStockProduct entity.
 */
public interface BadStockProductSearchRepository extends ElasticsearchRepository<BadStockProduct, Long> {
}
