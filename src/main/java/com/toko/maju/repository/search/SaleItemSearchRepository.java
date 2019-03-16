package com.toko.maju.repository.search;

import com.toko.maju.domain.SaleItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SaleItem entity.
 */
public interface SaleItemSearchRepository extends ElasticsearchRepository<SaleItem, Long> {
}
