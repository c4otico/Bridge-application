package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.Applications;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Applications} entity.
 */
public interface ApplicationsSearchRepository extends ElasticsearchRepository<Applications, Long> {
}
