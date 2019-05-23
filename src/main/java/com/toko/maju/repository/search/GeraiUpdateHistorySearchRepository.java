package com.toko.maju.repository.search;

import com.toko.maju.domain.GeraiUpdateHistory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GeraiUpdateHistory entity.
 */
public interface GeraiUpdateHistorySearchRepository extends ElasticsearchRepository<GeraiUpdateHistory, Long> {
}
