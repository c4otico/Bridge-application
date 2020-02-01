package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.Formula;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Formula} entity.
 */
public interface FormulaSearchRepository extends ElasticsearchRepository<Formula, Long> {
}
