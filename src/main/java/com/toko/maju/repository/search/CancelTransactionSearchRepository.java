package com.toko.maju.repository.search;

import com.toko.maju.domain.CancelTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CancelTransaction entity.
 */
public interface CancelTransactionSearchRepository extends ElasticsearchRepository<CancelTransaction, Long> {
}
