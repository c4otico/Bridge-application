package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.FormulaItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormulaItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormulaItemsRepository extends JpaRepository<FormulaItems, Long> {

}
