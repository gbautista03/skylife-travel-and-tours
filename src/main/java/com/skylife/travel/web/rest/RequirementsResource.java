package com.skylife.travel.web.rest;

import com.skylife.travel.domain.Requirements;
import com.skylife.travel.repository.RequirementsRepository;
import com.skylife.travel.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.skylife.travel.domain.Requirements}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RequirementsResource {

    private final Logger log = LoggerFactory.getLogger(RequirementsResource.class);

    private static final String ENTITY_NAME = "requirements";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequirementsRepository requirementsRepository;

    public RequirementsResource(RequirementsRepository requirementsRepository) {
        this.requirementsRepository = requirementsRepository;
    }

    /**
     * {@code POST  /requirements} : Create a new requirements.
     *
     * @param requirements the requirements to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new requirements, or with status {@code 400 (Bad Request)} if the requirements has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/requirements")
    public ResponseEntity<Requirements> createRequirements(@Valid @RequestBody Requirements requirements) throws URISyntaxException {
        log.debug("REST request to save Requirements : {}", requirements);
        if (requirements.getId() != null) {
            throw new BadRequestAlertException("A new requirements cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Requirements result = requirementsRepository.save(requirements);
        return ResponseEntity
            .created(new URI("/api/requirements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /requirements/:id} : Updates an existing requirements.
     *
     * @param id the id of the requirements to save.
     * @param requirements the requirements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requirements,
     * or with status {@code 400 (Bad Request)} if the requirements is not valid,
     * or with status {@code 500 (Internal Server Error)} if the requirements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/requirements/{id}")
    public ResponseEntity<Requirements> updateRequirements(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Requirements requirements
    ) throws URISyntaxException {
        log.debug("REST request to update Requirements : {}, {}", id, requirements);
        if (requirements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requirements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requirementsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Requirements result = requirementsRepository.save(requirements);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requirements.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /requirements/:id} : Partial updates given fields of an existing requirements, field will ignore if it is null
     *
     * @param id the id of the requirements to save.
     * @param requirements the requirements to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated requirements,
     * or with status {@code 400 (Bad Request)} if the requirements is not valid,
     * or with status {@code 404 (Not Found)} if the requirements is not found,
     * or with status {@code 500 (Internal Server Error)} if the requirements couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/requirements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Requirements> partialUpdateRequirements(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Requirements requirements
    ) throws URISyntaxException {
        log.debug("REST request to partial update Requirements partially : {}, {}", id, requirements);
        if (requirements.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, requirements.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requirementsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Requirements> result = requirementsRepository
            .findById(requirements.getId())
            .map(existingRequirements -> {
                if (requirements.getDestination() != null) {
                    existingRequirements.setDestination(requirements.getDestination());
                }
                if (requirements.getDescription() != null) {
                    existingRequirements.setDescription(requirements.getDescription());
                }

                return existingRequirements;
            })
            .map(requirementsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, requirements.getId().toString())
        );
    }

    /**
     * {@code GET  /requirements} : get all the requirements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requirements in body.
     */
    @GetMapping("/requirements")
    public List<Requirements> getAllRequirements() {
        log.debug("REST request to get all Requirements");
        return requirementsRepository.findAll();
    }

    /**
     * {@code GET  /requirements/:id} : get the "id" requirements.
     *
     * @param id the id of the requirements to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the requirements, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/requirements/{id}")
    public ResponseEntity<Requirements> getRequirements(@PathVariable Long id) {
        log.debug("REST request to get Requirements : {}", id);
        Optional<Requirements> requirements = requirementsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(requirements);
    }

    /**
     * {@code DELETE  /requirements/:id} : delete the "id" requirements.
     *
     * @param id the id of the requirements to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/requirements/{id}")
    public ResponseEntity<Void> deleteRequirements(@PathVariable Long id) {
        log.debug("REST request to delete Requirements : {}", id);
        requirementsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
