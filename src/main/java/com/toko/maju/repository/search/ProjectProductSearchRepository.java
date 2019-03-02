package com.toko.maju.repository.search;

import com.toko.maju.domain.ProjectProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ProjectProduct entity.
 */
public interface ProjectProductSearchRepository extends ElasticsearchRepository<ProjectProduct, Long> {
}
