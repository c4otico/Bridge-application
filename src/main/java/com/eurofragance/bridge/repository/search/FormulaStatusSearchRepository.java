package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.FormulaStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormulaStatus} entity.
 */
public interface FormulaStatusSearchRepository extends ElasticsearchRepository<FormulaStatus, Long> {
}
