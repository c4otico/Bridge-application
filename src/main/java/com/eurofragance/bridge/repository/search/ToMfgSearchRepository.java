package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.ToMfg;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ToMfg} entity.
 */
public interface ToMfgSearchRepository extends ElasticsearchRepository<ToMfg, Long> {
}
