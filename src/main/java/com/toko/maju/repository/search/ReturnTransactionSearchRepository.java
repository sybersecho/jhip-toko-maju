package com.toko.maju.repository.search;

import com.toko.maju.domain.ReturnTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReturnTransaction entity.
 */
public interface ReturnTransactionSearchRepository extends ElasticsearchRepository<ReturnTransaction, Long> {
}
