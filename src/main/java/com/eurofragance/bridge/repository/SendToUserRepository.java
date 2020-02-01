package com.eurofragance.bridge.repository;

import com.eurofragance.bridge.domain.SendToUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SendToUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SendToUserRepository extends JpaRepository<SendToUser, Long> {

}
