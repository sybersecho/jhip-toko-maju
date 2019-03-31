package com.toko.maju.repository.search;

import com.toko.maju.domain.SequenceNumber;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SequenceNumber entity.
 */
public interface SequenceNumberSearchRepository extends ElasticsearchRepository<SequenceNumber, Long> {
}
