package com.eurofragance.bridge.repository.search;

import com.eurofragance.bridge.domain.FormulaItems;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link FormulaItems} entity.
 */
public interface FormulaItemsSearchRepository extends ElasticsearchRepository<FormulaItems, Long> {
}
