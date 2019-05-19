package com.toko.maju.repository.search;

import com.toko.maju.domain.Purchase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Purchase entity.
 */
public interface PurchaseSearchRepository extends ElasticsearchRepository<Purchase, Long> {
}
