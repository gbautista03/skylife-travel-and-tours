package com.skylife.travel.web.rest;

import com.skylife.travel.domain.PackageInclusionsExclusions;
import com.skylife.travel.repository.PackageInclusionsExclusionsRepository;
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
 * REST controller for managing {@link com.skylife.travel.domain.PackageInclusionsExclusions}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PackageInclusionsExclusionsResource {

    private final Logger log = LoggerFactory.getLogger(PackageInclusionsExclusionsResource.class);

    private static final String ENTITY_NAME = "packageInclusionsExclusions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageInclusionsExclusionsRepository packageInclusionsExclusionsRepository;

    public PackageInclusionsExclusionsResource(PackageInclusionsExclusionsRepository packageInclusionsExclusionsRepository) {
        this.packageInclusionsExclusionsRepository = packageInclusionsExclusionsRepository;
    }

    /**
     * {@code POST  /package-inclusions-exclusions} : Create a new packageInclusionsExclusions.
     *
     * @param packageInclusionsExclusions the packageInclusionsExclusions to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageInclusionsExclusions, or with status {@code 400 (Bad Request)} if the packageInclusionsExclusions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/package-inclusions-exclusions")
    public ResponseEntity<PackageInclusionsExclusions> createPackageInclusionsExclusions(
        @Valid @RequestBody PackageInclusionsExclusions packageInclusionsExclusions
    ) throws URISyntaxException {
        log.debug("REST request to save PackageInclusionsExclusions : {}", packageInclusionsExclusions);
        if (packageInclusionsExclusions.getId() != null) {
            throw new BadRequestAlertException("A new packageInclusionsExclusions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackageInclusionsExclusions result = packageInclusionsExclusionsRepository.save(packageInclusionsExclusions);
        return ResponseEntity
            .created(new URI("/api/package-inclusions-exclusions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /package-inclusions-exclusions/:id} : Updates an existing packageInclusionsExclusions.
     *
     * @param id the id of the packageInclusionsExclusions to save.
     * @param packageInclusionsExclusions the packageInclusionsExclusions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageInclusionsExclusions,
     * or with status {@code 400 (Bad Request)} if the packageInclusionsExclusions is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageInclusionsExclusions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/package-inclusions-exclusions/{id}")
    public ResponseEntity<PackageInclusionsExclusions> updatePackageInclusionsExclusions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PackageInclusionsExclusions packageInclusionsExclusions
    ) throws URISyntaxException {
        log.debug("REST request to update PackageInclusionsExclusions : {}, {}", id, packageInclusionsExclusions);
        if (packageInclusionsExclusions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packageInclusionsExclusions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packageInclusionsExclusionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PackageInclusionsExclusions result = packageInclusionsExclusionsRepository.save(packageInclusionsExclusions);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, packageInclusionsExclusions.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /package-inclusions-exclusions/:id} : Partial updates given fields of an existing packageInclusionsExclusions, field will ignore if it is null
     *
     * @param id the id of the packageInclusionsExclusions to save.
     * @param packageInclusionsExclusions the packageInclusionsExclusions to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageInclusionsExclusions,
     * or with status {@code 400 (Bad Request)} if the packageInclusionsExclusions is not valid,
     * or with status {@code 404 (Not Found)} if the packageInclusionsExclusions is not found,
     * or with status {@code 500 (Internal Server Error)} if the packageInclusionsExclusions couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/package-inclusions-exclusions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PackageInclusionsExclusions> partialUpdatePackageInclusionsExclusions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PackageInclusionsExclusions packageInclusionsExclusions
    ) throws URISyntaxException {
        log.debug("REST request to partial update PackageInclusionsExclusions partially : {}, {}", id, packageInclusionsExclusions);
        if (packageInclusionsExclusions.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packageInclusionsExclusions.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packageInclusionsExclusionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PackageInclusionsExclusions> result = packageInclusionsExclusionsRepository
            .findById(packageInclusionsExclusions.getId())
            .map(existingPackageInclusionsExclusions -> {
                if (packageInclusionsExclusions.getDestination() != null) {
                    existingPackageInclusionsExclusions.setDestination(packageInclusionsExclusions.getDestination());
                }
                if (packageInclusionsExclusions.getInclusions() != null) {
                    existingPackageInclusionsExclusions.setInclusions(packageInclusionsExclusions.getInclusions());
                }
                if (packageInclusionsExclusions.getExclusions() != null) {
                    existingPackageInclusionsExclusions.setExclusions(packageInclusionsExclusions.getExclusions());
                }

                return existingPackageInclusionsExclusions;
            })
            .map(packageInclusionsExclusionsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, packageInclusionsExclusions.getId().toString())
        );
    }

    /**
     * {@code GET  /package-inclusions-exclusions} : get all the packageInclusionsExclusions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageInclusionsExclusions in body.
     */
    @GetMapping("/package-inclusions-exclusions")
    public List<PackageInclusionsExclusions> getAllPackageInclusionsExclusions() {
        log.debug("REST request to get all PackageInclusionsExclusions");
        return packageInclusionsExclusionsRepository.findAll();
    }

    /**
     * {@code GET  /package-inclusions-exclusions/:id} : get the "id" packageInclusionsExclusions.
     *
     * @param id the id of the packageInclusionsExclusions to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageInclusionsExclusions, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/package-inclusions-exclusions/{id}")
    public ResponseEntity<PackageInclusionsExclusions> getPackageInclusionsExclusions(@PathVariable Long id) {
        log.debug("REST request to get PackageInclusionsExclusions : {}", id);
        Optional<PackageInclusionsExclusions> packageInclusionsExclusions = packageInclusionsExclusionsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(packageInclusionsExclusions);
    }

    /**
     * {@code DELETE  /package-inclusions-exclusions/:id} : delete the "id" packageInclusionsExclusions.
     *
     * @param id the id of the packageInclusionsExclusions to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/package-inclusions-exclusions/{id}")
    public ResponseEntity<Void> deletePackageInclusionsExclusions(@PathVariable Long id) {
        log.debug("REST request to delete PackageInclusionsExclusions : {}", id);
        packageInclusionsExclusionsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
