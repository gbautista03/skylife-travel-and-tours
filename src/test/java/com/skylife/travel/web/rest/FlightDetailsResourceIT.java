package com.skylife.travel.web.rest;

import static com.skylife.travel.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skylife.travel.IntegrationTest;
import com.skylife.travel.domain.FlightDetails;
import com.skylife.travel.repository.FlightDetailsRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlightDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FlightDetailsResourceIT {

    private static final String DEFAULT_ORIGIN = "AAAAAAAAAA";
    private static final String UPDATED_ORIGIN = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_FLIGHT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FLIGHT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_CARRIER = "AAAAAAAAAA";
    private static final String UPDATED_CARRIER = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEPARTURE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEPARTURE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_ARRIVAL_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ARRIVAL_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/flight-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlightDetailsRepository flightDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlightDetailsMockMvc;

    private FlightDetails flightDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlightDetails createEntity(EntityManager em) {
        FlightDetails flightDetails = new FlightDetails()
            .origin(DEFAULT_ORIGIN)
            .destination(DEFAULT_DESTINATION)
            .flightNumber(DEFAULT_FLIGHT_NUMBER)
            .carrier(DEFAULT_CARRIER)
            .departureDate(DEFAULT_DEPARTURE_DATE)
            .arrivalDate(DEFAULT_ARRIVAL_DATE);
        return flightDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FlightDetails createUpdatedEntity(EntityManager em) {
        FlightDetails flightDetails = new FlightDetails()
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .carrier(UPDATED_CARRIER)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .arrivalDate(UPDATED_ARRIVAL_DATE);
        return flightDetails;
    }

    @BeforeEach
    public void initTest() {
        flightDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createFlightDetails() throws Exception {
        int databaseSizeBeforeCreate = flightDetailsRepository.findAll().size();
        // Create the FlightDetails
        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isCreated());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        FlightDetails testFlightDetails = flightDetailsList.get(flightDetailsList.size() - 1);
        assertThat(testFlightDetails.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testFlightDetails.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testFlightDetails.getFlightNumber()).isEqualTo(DEFAULT_FLIGHT_NUMBER);
        assertThat(testFlightDetails.getCarrier()).isEqualTo(DEFAULT_CARRIER);
        assertThat(testFlightDetails.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testFlightDetails.getArrivalDate()).isEqualTo(DEFAULT_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    void createFlightDetailsWithExistingId() throws Exception {
        // Create the FlightDetails with an existing ID
        flightDetails.setId(1L);

        int databaseSizeBeforeCreate = flightDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isBadRequest());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOriginIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightDetailsRepository.findAll().size();
        // set the field null
        flightDetails.setOrigin(null);

        // Create the FlightDetails, which fails.

        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isBadRequest());

        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightDetailsRepository.findAll().size();
        // set the field null
        flightDetails.setDestination(null);

        // Create the FlightDetails, which fails.

        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isBadRequest());

        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFlightNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightDetailsRepository.findAll().size();
        // set the field null
        flightDetails.setFlightNumber(null);

        // Create the FlightDetails, which fails.

        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isBadRequest());

        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCarrierIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightDetailsRepository.findAll().size();
        // set the field null
        flightDetails.setCarrier(null);

        // Create the FlightDetails, which fails.

        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isBadRequest());

        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDepartureDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = flightDetailsRepository.findAll().size();
        // set the field null
        flightDetails.setDepartureDate(null);

        // Create the FlightDetails, which fails.

        restFlightDetailsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isBadRequest());

        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFlightDetails() throws Exception {
        // Initialize the database
        flightDetailsRepository.saveAndFlush(flightDetails);

        // Get all the flightDetailsList
        restFlightDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flightDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].flightNumber").value(hasItem(DEFAULT_FLIGHT_NUMBER)))
            .andExpect(jsonPath("$.[*].carrier").value(hasItem(DEFAULT_CARRIER)))
            .andExpect(jsonPath("$.[*].departureDate").value(hasItem(sameInstant(DEFAULT_DEPARTURE_DATE))))
            .andExpect(jsonPath("$.[*].arrivalDate").value(hasItem(sameInstant(DEFAULT_ARRIVAL_DATE))));
    }

    @Test
    @Transactional
    void getFlightDetails() throws Exception {
        // Initialize the database
        flightDetailsRepository.saveAndFlush(flightDetails);

        // Get the flightDetails
        restFlightDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, flightDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flightDetails.getId().intValue()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.flightNumber").value(DEFAULT_FLIGHT_NUMBER))
            .andExpect(jsonPath("$.carrier").value(DEFAULT_CARRIER))
            .andExpect(jsonPath("$.departureDate").value(sameInstant(DEFAULT_DEPARTURE_DATE)))
            .andExpect(jsonPath("$.arrivalDate").value(sameInstant(DEFAULT_ARRIVAL_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingFlightDetails() throws Exception {
        // Get the flightDetails
        restFlightDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFlightDetails() throws Exception {
        // Initialize the database
        flightDetailsRepository.saveAndFlush(flightDetails);

        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();

        // Update the flightDetails
        FlightDetails updatedFlightDetails = flightDetailsRepository.findById(flightDetails.getId()).get();
        // Disconnect from session so that the updates on updatedFlightDetails are not directly saved in db
        em.detach(updatedFlightDetails);
        updatedFlightDetails
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .carrier(UPDATED_CARRIER)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .arrivalDate(UPDATED_ARRIVAL_DATE);

        restFlightDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlightDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlightDetails))
            )
            .andExpect(status().isOk());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
        FlightDetails testFlightDetails = flightDetailsList.get(flightDetailsList.size() - 1);
        assertThat(testFlightDetails.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFlightDetails.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testFlightDetails.getFlightNumber()).isEqualTo(UPDATED_FLIGHT_NUMBER);
        assertThat(testFlightDetails.getCarrier()).isEqualTo(UPDATED_CARRIER);
        assertThat(testFlightDetails.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testFlightDetails.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFlightDetails() throws Exception {
        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();
        flightDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flightDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flightDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlightDetails() throws Exception {
        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();
        flightDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flightDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlightDetails() throws Exception {
        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();
        flightDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightDetailsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flightDetails)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlightDetailsWithPatch() throws Exception {
        // Initialize the database
        flightDetailsRepository.saveAndFlush(flightDetails);

        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();

        // Update the flightDetails using partial update
        FlightDetails partialUpdatedFlightDetails = new FlightDetails();
        partialUpdatedFlightDetails.setId(flightDetails.getId());

        partialUpdatedFlightDetails
            .origin(UPDATED_ORIGIN)
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .carrier(UPDATED_CARRIER)
            .arrivalDate(UPDATED_ARRIVAL_DATE);

        restFlightDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlightDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlightDetails))
            )
            .andExpect(status().isOk());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
        FlightDetails testFlightDetails = flightDetailsList.get(flightDetailsList.size() - 1);
        assertThat(testFlightDetails.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFlightDetails.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testFlightDetails.getFlightNumber()).isEqualTo(UPDATED_FLIGHT_NUMBER);
        assertThat(testFlightDetails.getCarrier()).isEqualTo(UPDATED_CARRIER);
        assertThat(testFlightDetails.getDepartureDate()).isEqualTo(DEFAULT_DEPARTURE_DATE);
        assertThat(testFlightDetails.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFlightDetailsWithPatch() throws Exception {
        // Initialize the database
        flightDetailsRepository.saveAndFlush(flightDetails);

        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();

        // Update the flightDetails using partial update
        FlightDetails partialUpdatedFlightDetails = new FlightDetails();
        partialUpdatedFlightDetails.setId(flightDetails.getId());

        partialUpdatedFlightDetails
            .origin(UPDATED_ORIGIN)
            .destination(UPDATED_DESTINATION)
            .flightNumber(UPDATED_FLIGHT_NUMBER)
            .carrier(UPDATED_CARRIER)
            .departureDate(UPDATED_DEPARTURE_DATE)
            .arrivalDate(UPDATED_ARRIVAL_DATE);

        restFlightDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlightDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlightDetails))
            )
            .andExpect(status().isOk());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
        FlightDetails testFlightDetails = flightDetailsList.get(flightDetailsList.size() - 1);
        assertThat(testFlightDetails.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testFlightDetails.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testFlightDetails.getFlightNumber()).isEqualTo(UPDATED_FLIGHT_NUMBER);
        assertThat(testFlightDetails.getCarrier()).isEqualTo(UPDATED_CARRIER);
        assertThat(testFlightDetails.getDepartureDate()).isEqualTo(UPDATED_DEPARTURE_DATE);
        assertThat(testFlightDetails.getArrivalDate()).isEqualTo(UPDATED_ARRIVAL_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFlightDetails() throws Exception {
        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();
        flightDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flightDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flightDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlightDetails() throws Exception {
        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();
        flightDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flightDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlightDetails() throws Exception {
        int databaseSizeBeforeUpdate = flightDetailsRepository.findAll().size();
        flightDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flightDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FlightDetails in the database
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlightDetails() throws Exception {
        // Initialize the database
        flightDetailsRepository.saveAndFlush(flightDetails);

        int databaseSizeBeforeDelete = flightDetailsRepository.findAll().size();

        // Delete the flightDetails
        restFlightDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, flightDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FlightDetails> flightDetailsList = flightDetailsRepository.findAll();
        assertThat(flightDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
