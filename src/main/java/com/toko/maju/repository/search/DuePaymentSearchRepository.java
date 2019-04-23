package com.toko.maju.repository.search;

import com.toko.maju.domain.DuePayment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the DuePayment entity.
 */
public interface DuePaymentSearchRepository extends ElasticsearchRepository<DuePayment, Long> {
}
