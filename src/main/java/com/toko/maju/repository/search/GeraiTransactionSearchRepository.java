package com.toko.maju.repository.search;

import com.toko.maju.domain.GeraiTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GeraiTransaction entity.
 */
public interface GeraiTransactionSearchRepository extends ElasticsearchRepository<GeraiTransaction, Long> {
}
