package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.SendToUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link SendToUser} entity.
 */
public interface SendToUserSearchRepository extends ElasticsearchRepository<SendToUser, Long> {
}
