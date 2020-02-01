package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.FormulaReg;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormulaReg} entity.
 */
public interface FormulaRegSearchRepository extends ElasticsearchRepository<FormulaReg, Long> {
}
