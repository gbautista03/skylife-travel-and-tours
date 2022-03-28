package com.skylife.travel.web.rest;

import com.skylife.travel.domain.PackageTour;
import com.skylife.travel.repository.PackageTourRepository;
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
 * REST controller for managing {@link com.skylife.travel.domain.PackageTour}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PackageTourResource {

    private final Logger log = LoggerFactory.getLogger(PackageTourResource.class);

    private static final String ENTITY_NAME = "packageTour";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PackageTourRepository packageTourRepository;

    public PackageTourResource(PackageTourRepository packageTourRepository) {
        this.packageTourRepository = packageTourRepository;
    }

    /**
     * {@code POST  /package-tours} : Create a new packageTour.
     *
     * @param packageTour the packageTour to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new packageTour, or with status {@code 400 (Bad Request)} if the packageTour has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/package-tours")
    public ResponseEntity<PackageTour> createPackageTour(@Valid @RequestBody PackageTour packageTour) throws URISyntaxException {
        log.debug("REST request to save PackageTour : {}", packageTour);
        if (packageTour.getId() != null) {
            throw new BadRequestAlertException("A new packageTour cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PackageTour result = packageTourRepository.save(packageTour);
        return ResponseEntity
            .created(new URI("/api/package-tours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /package-tours/:id} : Updates an existing packageTour.
     *
     * @param id the id of the packageTour to save.
     * @param packageTour the packageTour to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageTour,
     * or with status {@code 400 (Bad Request)} if the packageTour is not valid,
     * or with status {@code 500 (Internal Server Error)} if the packageTour couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/package-tours/{id}")
    public ResponseEntity<PackageTour> updatePackageTour(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PackageTour packageTour
    ) throws URISyntaxException {
        log.debug("REST request to update PackageTour : {}, {}", id, packageTour);
        if (packageTour.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packageTour.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packageTourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PackageTour result = packageTourRepository.save(packageTour);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, packageTour.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /package-tours/:id} : Partial updates given fields of an existing packageTour, field will ignore if it is null
     *
     * @param id the id of the packageTour to save.
     * @param packageTour the packageTour to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated packageTour,
     * or with status {@code 400 (Bad Request)} if the packageTour is not valid,
     * or with status {@code 404 (Not Found)} if the packageTour is not found,
     * or with status {@code 500 (Internal Server Error)} if the packageTour couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/package-tours/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PackageTour> partialUpdatePackageTour(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PackageTour packageTour
    ) throws URISyntaxException {
        log.debug("REST request to partial update PackageTour partially : {}, {}", id, packageTour);
        if (packageTour.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, packageTour.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!packageTourRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PackageTour> result = packageTourRepository
            .findById(packageTour.getId())
            .map(existingPackageTour -> {
                if (packageTour.getDays() != null) {
                    existingPackageTour.setDays(packageTour.getDays());
                }
                if (packageTour.getNights() != null) {
                    existingPackageTour.setNights(packageTour.getNights());
                }
                if (packageTour.getDestination() != null) {
                    existingPackageTour.setDestination(packageTour.getDestination());
                }
                if (packageTour.getTourCode() != null) {
                    existingPackageTour.setTourCode(packageTour.getTourCode());
                }
                if (packageTour.getDate() != null) {
                    existingPackageTour.setDate(packageTour.getDate());
                }
                if (packageTour.getHotel() != null) {
                    existingPackageTour.setHotel(packageTour.getHotel());
                }
                if (packageTour.getRoomType() != null) {
                    existingPackageTour.setRoomType(packageTour.getRoomType());
                }
                if (packageTour.getNumberOfGuest() != null) {
                    existingPackageTour.setNumberOfGuest(packageTour.getNumberOfGuest());
                }

                return existingPackageTour;
            })
            .map(packageTourRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, packageTour.getId().toString())
        );
    }

    /**
     * {@code GET  /package-tours} : get all the packageTours.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of packageTours in body.
     */
    @GetMapping("/package-tours")
    public List<PackageTour> getAllPackageTours(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PackageTours");
        return packageTourRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /package-tours/:id} : get the "id" packageTour.
     *
     * @param id the id of the packageTour to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the packageTour, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/package-tours/{id}")
    public ResponseEntity<PackageTour> getPackageTour(@PathVariable Long id) {
        log.debug("REST request to get PackageTour : {}", id);
        Optional<PackageTour> packageTour = packageTourRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(packageTour);
    }

    /**
     * {@code DELETE  /package-tours/:id} : delete the "id" packageTour.
     *
     * @param id the id of the packageTour to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/package-tours/{id}")
    public ResponseEntity<Void> deletePackageTour(@PathVariable Long id) {
        log.debug("REST request to delete PackageTour : {}", id);
        packageTourRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
