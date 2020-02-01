package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.Developer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Developer} entity.
 */
public interface DeveloperSearchRepository extends ElasticsearchRepository<Developer, Long> {
}
