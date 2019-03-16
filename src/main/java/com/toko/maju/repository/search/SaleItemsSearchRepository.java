package com.toko.maju.repository.search;

import com.toko.maju.domain.SaleItems;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SaleItems entity.
 */
public interface SaleItemsSearchRepository extends ElasticsearchRepository<SaleItems, Long> {
}
