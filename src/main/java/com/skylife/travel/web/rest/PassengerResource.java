package com.skylife.travel.web.rest;

import com.skylife.travel.domain.Passenger;
import com.skylife.travel.repository.PassengerRepository;
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
 * REST controller for managing {@link com.skylife.travel.domain.Passenger}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PassengerResource {

    private final Logger log = LoggerFactory.getLogger(PassengerResource.class);

    private static final String ENTITY_NAME = "passenger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassengerRepository passengerRepository;

    public PassengerResource(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    /**
     * {@code POST  /passengers} : Create a new passenger.
     *
     * @param passenger the passenger to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passenger, or with status {@code 400 (Bad Request)} if the passenger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passengers")
    public ResponseEntity<Passenger> createPassenger(@Valid @RequestBody Passenger passenger) throws URISyntaxException {
        log.debug("REST request to save Passenger : {}", passenger);
        if (passenger.getId() != null) {
            throw new BadRequestAlertException("A new passenger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Passenger result = passengerRepository.save(passenger);
        return ResponseEntity
            .created(new URI("/api/passengers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passengers/:id} : Updates an existing passenger.
     *
     * @param id the id of the passenger to save.
     * @param passenger the passenger to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passenger,
     * or with status {@code 400 (Bad Request)} if the passenger is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passenger couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passengers/{id}")
    public ResponseEntity<Passenger> updatePassenger(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Passenger passenger
    ) throws URISyntaxException {
        log.debug("REST request to update Passenger : {}, {}", id, passenger);
        if (passenger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passenger.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passengerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Passenger result = passengerRepository.save(passenger);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passenger.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /passengers/:id} : Partial updates given fields of an existing passenger, field will ignore if it is null
     *
     * @param id the id of the passenger to save.
     * @param passenger the passenger to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passenger,
     * or with status {@code 400 (Bad Request)} if the passenger is not valid,
     * or with status {@code 404 (Not Found)} if the passenger is not found,
     * or with status {@code 500 (Internal Server Error)} if the passenger couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/passengers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Passenger> partialUpdatePassenger(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Passenger passenger
    ) throws URISyntaxException {
        log.debug("REST request to partial update Passenger partially : {}, {}", id, passenger);
        if (passenger.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passenger.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passengerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Passenger> result = passengerRepository
            .findById(passenger.getId())
            .map(existingPassenger -> {
                if (passenger.getFirstName() != null) {
                    existingPassenger.setFirstName(passenger.getFirstName());
                }
                if (passenger.getLastName() != null) {
                    existingPassenger.setLastName(passenger.getLastName());
                }
                if (passenger.getBirthday() != null) {
                    existingPassenger.setBirthday(passenger.getBirthday());
                }
                if (passenger.getGender() != null) {
                    existingPassenger.setGender(passenger.getGender());
                }
                if (passenger.getCitizenship() != null) {
                    existingPassenger.setCitizenship(passenger.getCitizenship());
                }
                if (passenger.getContactNumber() != null) {
                    existingPassenger.setContactNumber(passenger.getContactNumber());
                }
                if (passenger.getEmailAddress() != null) {
                    existingPassenger.setEmailAddress(passenger.getEmailAddress());
                }

                return existingPassenger;
            })
            .map(passengerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passenger.getId().toString())
        );
    }

    /**
     * {@code GET  /passengers} : get all the passengers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passengers in body.
     */
    @GetMapping("/passengers")
    public List<Passenger> getAllPassengers() {
        log.debug("REST request to get all Passengers");
        return passengerRepository.findAll();
    }

    /**
     * {@code GET  /passengers/:id} : get the "id" passenger.
     *
     * @param id the id of the passenger to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passenger, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passengers/{id}")
    public ResponseEntity<Passenger> getPassenger(@PathVariable Long id) {
        log.debug("REST request to get Passenger : {}", id);
        Optional<Passenger> passenger = passengerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(passenger);
    }

    /**
     * {@code DELETE  /passengers/:id} : delete the "id" passenger.
     *
     * @param id the id of the passenger to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passengers/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        log.debug("REST request to delete Passenger : {}", id);
        passengerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
