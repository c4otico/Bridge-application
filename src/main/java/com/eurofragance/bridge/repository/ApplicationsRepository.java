package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.Applications;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Applications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationsRepository extends JpaRepository<Applications, Long> {

}
