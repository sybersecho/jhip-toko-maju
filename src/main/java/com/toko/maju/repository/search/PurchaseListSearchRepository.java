package com.toko.maju.repository.search;

import com.toko.maju.domain.PurchaseList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the PurchaseList entity.
 */
public interface PurchaseListSearchRepository extends ElasticsearchRepository<PurchaseList, Long> {
}
