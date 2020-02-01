package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.FormulaStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormulaStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormulaStatusRepository extends JpaRepository<FormulaStatus, Long> {

}
