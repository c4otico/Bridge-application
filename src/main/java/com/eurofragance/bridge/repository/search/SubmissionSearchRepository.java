package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.Submission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Submission} entity.
 */
public interface SubmissionSearchRepository extends ElasticsearchRepository<Submission, Long> {
}
