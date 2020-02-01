package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.Developer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Developer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
