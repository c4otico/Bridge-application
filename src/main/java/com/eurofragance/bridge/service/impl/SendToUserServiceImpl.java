package com.eurofragance.bridge.service.impl;

import com.eurofragance.bridge.service.SendToUserService;
import com.eurofragance.bridge.domain.SendToUser;
import com.eurofragance.bridge.repository.SendToUserRepository;
import com.eurofragance.bridge.repository.search.SendToUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link SendToUser}.
 */
@Service
@Transactional
public class SendToUserServiceImpl implements SendToUserService {

    private final Logger log = LoggerFactory.getLogger(SendToUserServiceImpl.class);

    private final SendToUserRepository sendToUserRepository;

    private final SendToUserSearchRepository sendToUserSearchRepository;

    public SendToUserServiceImpl(SendToUserRepository sendToUserRepository, SendToUserSearchRepository sendToUserSearchRepository) {
        this.sendToUserRepository = sendToUserRepository;
        this.sendToUserSearchRepository = sendToUserSearchRepository;
    }

    /**
     * Save a sendToUser.
     *
     * @param sendToUser the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SendToUser save(SendToUser sendToUser) {
        log.debug("Request to save SendToUser : {}", sendToUser);
        SendToUser result = sendToUserRepository.save(sendToUser);
        sendToUserSearchRepository.save(result);
        return result;
    }

    /**
     * Get all the sendToUsers.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SendToUser> findAll() {
        log.debug("Request to get all SendToUsers");
        return sendToUserRepository.findAll();
    }


    /**
     * Get one sendToUser by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SendToUser> findOne(Long id) {
        log.debug("Request to get SendToUser : {}", id);
        return sendToUserRepository.findById(id);
    }

    /**
     * Delete the sendToUser by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete SendToUser : {}", id);
        sendToUserRepository.deleteById(id);
        sendToUserSearchRepository.deleteById(id);
    }

    /**
     * Search for the sendToUser corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SendToUser> search(String query) {
        log.debug("Request to search SendToUsers for query {}", query);
        return StreamSupport
            .stream(sendToUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
