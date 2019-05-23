package com.toko.maju.repository.search;

import com.toko.maju.domain.GeraiConfig;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the GeraiConfig entity.
 */
public interface GeraiConfigSearchRepository extends ElasticsearchRepository<GeraiConfig, Long> {
}
