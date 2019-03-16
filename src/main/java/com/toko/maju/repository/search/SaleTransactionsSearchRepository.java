package com.toko.maju.repository.search;

import com.toko.maju.domain.SaleTransactions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SaleTransactions entity.
 */
public interface SaleTransactionsSearchRepository extends ElasticsearchRepository<SaleTransactions, Long> {
}
