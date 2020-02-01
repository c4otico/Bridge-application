package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.FormulaReg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FormulaReg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormulaRegRepository extends JpaRepository<FormulaReg, Long> {

}
