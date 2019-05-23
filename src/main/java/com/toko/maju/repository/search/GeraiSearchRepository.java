package com.toko.maju.repository.search;

import com.toko.maju.domain.Gerai;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Gerai entity.
 */
public interface GeraiSearchRepository extends ElasticsearchRepository<Gerai, Long> {
}
