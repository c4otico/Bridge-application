package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.SendToUser;
import com.eurofragance.bridge.service.SendToUserService;
import com.eurofragance.bridge.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.eurofragance.bridge.domain.SendToUser}.
 */
@RestController
@RequestMapping("/api")
public class SendToUserResource {

    private final Logger log = LoggerFactory.getLogger(SendToUserResource.class);

    private static final String ENTITY_NAME = "sendToUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SendToUserService sendToUserService;

    public SendToUserResource(SendToUserService sendToUserService) {
        this.sendToUserService = sendToUserService;
    }

    /**
     * {@code POST  /send-to-users} : Create a new sendToUser.
     *
     * @param sendToUser the sendToUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sendToUser, or with status {@code 400 (Bad Request)} if the sendToUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/send-to-users")
    public ResponseEntity<SendToUser> createSendToUser(@RequestBody SendToUser sendToUser) throws URISyntaxException {
        log.debug("REST request to save SendToUser : {}", sendToUser);
        if (sendToUser.getId() != null) {
            throw new BadRequestAlertException("A new sendToUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SendToUser result = sendToUserService.save(sendToUser);
        return ResponseEntity.created(new URI("/api/send-to-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /send-to-users} : Updates an existing sendToUser.
     *
     * @param sendToUser the sendToUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sendToUser,
     * or with status {@code 400 (Bad Request)} if the sendToUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sendToUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/send-to-users")
    public ResponseEntity<SendToUser> updateSendToUser(@RequestBody SendToUser sendToUser) throws URISyntaxException {
        log.debug("REST request to update SendToUser : {}", sendToUser);
        if (sendToUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SendToUser result = sendToUserService.save(sendToUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sendToUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /send-to-users} : get all the sendToUsers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sendToUsers in body.
     */
    @GetMapping("/send-to-users")
    public List<SendToUser> getAllSendToUsers() {
        log.debug("REST request to get all SendToUsers");
        return sendToUserService.findAll();
    }

    /**
     * {@code GET  /send-to-users/:id} : get the "id" sendToUser.
     *
     * @param id the id of the sendToUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sendToUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/send-to-users/{id}")
    public ResponseEntity<SendToUser> getSendToUser(@PathVariable Long id) {
        log.debug("REST request to get SendToUser : {}", id);
        Optional<SendToUser> sendToUser = sendToUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sendToUser);
    }

    /**
     * {@code DELETE  /send-to-users/:id} : delete the "id" sendToUser.
     *
     * @param id the id of the sendToUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/send-to-users/{id}")
    public ResponseEntity<Void> deleteSendToUser(@PathVariable Long id) {
        log.debug("REST request to delete SendToUser : {}", id);
        sendToUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/send-to-users?query=:query} : search for the sendToUser corresponding
     * to the query.
     *
     * @param query the query of the sendToUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/send-to-users")
    public List<SendToUser> searchSendToUsers(@RequestParam String query) {
        log.debug("REST request to search SendToUsers for query {}", query);
        return sendToUserService.search(query);
    }
}
