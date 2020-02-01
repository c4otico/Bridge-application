package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.ToMfg;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ToMfg entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToMfgRepository extends JpaRepository<ToMfg, Long> {

}
