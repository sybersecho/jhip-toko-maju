package com.toko.maju.repository.search;

import com.toko.maju.domain.CustomerProduct;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CustomerProduct entity.
 */
public interface CustomerProductSearchRepository extends ElasticsearchRepository<CustomerProduct, Long> {
}
