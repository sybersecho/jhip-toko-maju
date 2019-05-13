package com.toko.maju.repository.search;

import com.toko.maju.domain.ReturnItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ReturnItem entity.
 */
public interface ReturnItemSearchRepository extends ElasticsearchRepository<ReturnItem, Long> {
}
