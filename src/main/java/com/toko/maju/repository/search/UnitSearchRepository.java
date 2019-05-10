package com.toko.maju.repository.search;

import com.toko.maju.domain.Unit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Unit} entity.
 */
public interface UnitSearchRepository extends ElasticsearchRepository<Unit, Long> {
}
