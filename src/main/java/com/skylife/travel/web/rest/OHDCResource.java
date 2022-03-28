package com.skylife.travel.web.rest;

import com.skylife.travel.domain.OHDC;
import com.skylife.travel.repository.OHDCRepository;
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
 * REST controller for managing {@link com.skylife.travel.domain.OHDC}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OHDCResource {

    private final Logger log = LoggerFactory.getLogger(OHDCResource.class);

    private static final String ENTITY_NAME = "oHDC";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OHDCRepository oHDCRepository;

    public OHDCResource(OHDCRepository oHDCRepository) {
        this.oHDCRepository = oHDCRepository;
    }

    /**
     * {@code POST  /ohdcs} : Create a new oHDC.
     *
     * @param oHDC the oHDC to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new oHDC, or with status {@code 400 (Bad Request)} if the oHDC has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ohdcs")
    public ResponseEntity<OHDC> createOHDC(@Valid @RequestBody OHDC oHDC) throws URISyntaxException {
        log.debug("REST request to save OHDC : {}", oHDC);
        if (oHDC.getId() != null) {
            throw new BadRequestAlertException("A new oHDC cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OHDC result = oHDCRepository.save(oHDC);
        return ResponseEntity
            .created(new URI("/api/ohdcs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ohdcs/:id} : Updates an existing oHDC.
     *
     * @param id the id of the oHDC to save.
     * @param oHDC the oHDC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oHDC,
     * or with status {@code 400 (Bad Request)} if the oHDC is not valid,
     * or with status {@code 500 (Internal Server Error)} if the oHDC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ohdcs/{id}")
    public ResponseEntity<OHDC> updateOHDC(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody OHDC oHDC)
        throws URISyntaxException {
        log.debug("REST request to update OHDC : {}, {}", id, oHDC);
        if (oHDC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oHDC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oHDCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OHDC result = oHDCRepository.save(oHDC);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oHDC.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ohdcs/:id} : Partial updates given fields of an existing oHDC, field will ignore if it is null
     *
     * @param id the id of the oHDC to save.
     * @param oHDC the oHDC to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated oHDC,
     * or with status {@code 400 (Bad Request)} if the oHDC is not valid,
     * or with status {@code 404 (Not Found)} if the oHDC is not found,
     * or with status {@code 500 (Internal Server Error)} if the oHDC couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ohdcs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OHDC> partialUpdateOHDC(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OHDC oHDC
    ) throws URISyntaxException {
        log.debug("REST request to partial update OHDC partially : {}, {}", id, oHDC);
        if (oHDC.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, oHDC.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!oHDCRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OHDC> result = oHDCRepository
            .findById(oHDC.getId())
            .map(existingOHDC -> {
                if (oHDC.getDestination() != null) {
                    existingOHDC.setDestination(oHDC.getDestination());
                }
                if (oHDC.getDescription() != null) {
                    existingOHDC.setDescription(oHDC.getDescription());
                }
                if (oHDC.getContactDescription() != null) {
                    existingOHDC.setContactDescription(oHDC.getContactDescription());
                }

                return existingOHDC;
            })
            .map(oHDCRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, oHDC.getId().toString())
        );
    }

    /**
     * {@code GET  /ohdcs} : get all the oHDCS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of oHDCS in body.
     */
    @GetMapping("/ohdcs")
    public List<OHDC> getAllOHDCS() {
        log.debug("REST request to get all OHDCS");
        return oHDCRepository.findAll();
    }

    /**
     * {@code GET  /ohdcs/:id} : get the "id" oHDC.
     *
     * @param id the id of the oHDC to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the oHDC, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ohdcs/{id}")
    public ResponseEntity<OHDC> getOHDC(@PathVariable Long id) {
        log.debug("REST request to get OHDC : {}", id);
        Optional<OHDC> oHDC = oHDCRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(oHDC);
    }

    /**
     * {@code DELETE  /ohdcs/:id} : delete the "id" oHDC.
     *
     * @param id the id of the oHDC to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ohdcs/{id}")
    public ResponseEntity<Void> deleteOHDC(@PathVariable Long id) {
        log.debug("REST request to delete OHDC : {}", id);
        oHDCRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
