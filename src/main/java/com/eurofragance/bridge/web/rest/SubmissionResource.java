package com.eurofragance.bridge.web.rest;

import com.eurofragance.bridge.domain.Submission;
import com.eurofragance.bridge.service.SubmissionService;
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
 * REST controller for managing {@link com.eurofragance.bridge.domain.Submission}.
 */
@RestController
@RequestMapping("/api")
public class SubmissionResource {

    private final Logger log = LoggerFactory.getLogger(SubmissionResource.class);

    private static final String ENTITY_NAME = "submission";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubmissionService submissionService;

    public SubmissionResource(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    /**
     * {@code POST  /submissions} : Create a new submission.
     *
     * @param submission the submission to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new submission, or with status {@code 400 (Bad Request)} if the submission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/submissions")
    public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission) throws URISyntaxException {
        log.debug("REST request to save Submission : {}", submission);
        if (submission.getId() != null) {
            throw new BadRequestAlertException("A new submission cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Submission result = submissionService.save(submission);
        return ResponseEntity.created(new URI("/api/submissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /submissions} : Updates an existing submission.
     *
     * @param submission the submission to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated submission,
     * or with status {@code 400 (Bad Request)} if the submission is not valid,
     * or with status {@code 500 (Internal Server Error)} if the submission couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/submissions")
    public ResponseEntity<Submission> updateSubmission(@RequestBody Submission submission) throws URISyntaxException {
        log.debug("REST request to update Submission : {}", submission);
        if (submission.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Submission result = submissionService.save(submission);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, submission.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /submissions} : get all the submissions.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of submissions in body.
     */
    @GetMapping("/submissions")
    public List<Submission> getAllSubmissions() {
        log.debug("REST request to get all Submissions");
        return submissionService.findAll();
    }

    /**
     * {@code GET  /submissions/:id} : get the "id" submission.
     *
     * @param id the id of the submission to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the submission, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/submissions/{id}")
    public ResponseEntity<Submission> getSubmission(@PathVariable Long id) {
        log.debug("REST request to get Submission : {}", id);
        Optional<Submission> submission = submissionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(submission);
    }

    /**
     * {@code DELETE  /submissions/:id} : delete the "id" submission.
     *
     * @param id the id of the submission to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/submissions/{id}")
    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
        log.debug("REST request to delete Submission : {}", id);
        submissionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/submissions?query=:query} : search for the submission corresponding
     * to the query.
     *
     * @param query the query of the submission search.
     * @return the result of the search.
     */
    @GetMapping("/_search/submissions")
    public List<Submission> searchSubmissions(@RequestParam String query) {
        log.debug("REST request to search Submissions for query {}", query);
        return submissionService.search(query);
    }
}
