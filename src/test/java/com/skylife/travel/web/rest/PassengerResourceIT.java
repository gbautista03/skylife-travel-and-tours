package com.skylife.travel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skylife.travel.IntegrationTest;
import com.skylife.travel.domain.Passenger;
import com.skylife.travel.repository.PassengerRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PassengerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PassengerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_BIRTHDAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_BIRTHDAY = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final String DEFAULT_CITIZENSHIP = "AAAAAAAAAA";
    private static final String UPDATED_CITIZENSHIP = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/passengers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassengerMockMvc;

    private Passenger passenger;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .birthday(DEFAULT_BIRTHDAY)
            .gender(DEFAULT_GENDER)
            .citizenship(DEFAULT_CITIZENSHIP)
            .contactNumber(DEFAULT_CONTACT_NUMBER)
            .emailAddress(DEFAULT_EMAIL_ADDRESS);
        return passenger;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passenger createUpdatedEntity(EntityManager em) {
        Passenger passenger = new Passenger()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .gender(UPDATED_GENDER)
            .citizenship(UPDATED_CITIZENSHIP)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS);
        return passenger;
    }

    @BeforeEach
    public void initTest() {
        passenger = createEntity(em);
    }

    @Test
    @Transactional
    void createPassenger() throws Exception {
        int databaseSizeBeforeCreate = passengerRepository.findAll().size();
        // Create the Passenger
        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isCreated());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate + 1);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPassenger.getBirthday()).isEqualTo(DEFAULT_BIRTHDAY);
        assertThat(testPassenger.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPassenger.getCitizenship()).isEqualTo(DEFAULT_CITIZENSHIP);
        assertThat(testPassenger.getContactNumber()).isEqualTo(DEFAULT_CONTACT_NUMBER);
        assertThat(testPassenger.getEmailAddress()).isEqualTo(DEFAULT_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void createPassengerWithExistingId() throws Exception {
        // Create the Passenger with an existing ID
        passenger.setId(1L);

        int databaseSizeBeforeCreate = passengerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setFirstName(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setLastName(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthdayIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setBirthday(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setGender(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCitizenshipIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setCitizenship(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContactNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setContactNumber(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = passengerRepository.findAll().size();
        // set the field null
        passenger.setEmailAddress(null);

        // Create the Passenger, which fails.

        restPassengerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isBadRequest());

        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPassengers() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get all the passengerList
        restPassengerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passenger.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].birthday").value(hasItem(DEFAULT_BIRTHDAY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].citizenship").value(hasItem(DEFAULT_CITIZENSHIP)))
            .andExpect(jsonPath("$.[*].contactNumber").value(hasItem(DEFAULT_CONTACT_NUMBER)))
            .andExpect(jsonPath("$.[*].emailAddress").value(hasItem(DEFAULT_EMAIL_ADDRESS)));
    }

    @Test
    @Transactional
    void getPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        // Get the passenger
        restPassengerMockMvc
            .perform(get(ENTITY_API_URL_ID, passenger.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passenger.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.birthday").value(DEFAULT_BIRTHDAY.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.citizenship").value(DEFAULT_CITIZENSHIP))
            .andExpect(jsonPath("$.contactNumber").value(DEFAULT_CONTACT_NUMBER))
            .andExpect(jsonPath("$.emailAddress").value(DEFAULT_EMAIL_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingPassenger() throws Exception {
        // Get the passenger
        restPassengerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger
        Passenger updatedPassenger = passengerRepository.findById(passenger.getId()).get();
        // Disconnect from session so that the updates on updatedPassenger are not directly saved in db
        em.detach(updatedPassenger);
        updatedPassenger
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .gender(UPDATED_GENDER)
            .citizenship(UPDATED_CITIZENSHIP)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS);

        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPassenger.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPassenger.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPassenger.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPassenger.getCitizenship()).isEqualTo(UPDATED_CITIZENSHIP);
        assertThat(testPassenger.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testPassenger.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void putNonExistingPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();
        passenger.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passenger.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passenger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();
        passenger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passenger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();
        passenger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passenger)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassengerWithPatch() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger using partial update
        Passenger partialUpdatedPassenger = new Passenger();
        partialUpdatedPassenger.setId(passenger.getId());

        partialUpdatedPassenger.birthday(UPDATED_BIRTHDAY).contactNumber(UPDATED_CONTACT_NUMBER).emailAddress(UPDATED_EMAIL_ADDRESS);

        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testPassenger.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPassenger.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testPassenger.getCitizenship()).isEqualTo(DEFAULT_CITIZENSHIP);
        assertThat(testPassenger.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testPassenger.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void fullUpdatePassengerWithPatch() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();

        // Update the passenger using partial update
        Passenger partialUpdatedPassenger = new Passenger();
        partialUpdatedPassenger.setId(passenger.getId());

        partialUpdatedPassenger
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .birthday(UPDATED_BIRTHDAY)
            .gender(UPDATED_GENDER)
            .citizenship(UPDATED_CITIZENSHIP)
            .contactNumber(UPDATED_CONTACT_NUMBER)
            .emailAddress(UPDATED_EMAIL_ADDRESS);

        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPassenger))
            )
            .andExpect(status().isOk());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
        Passenger testPassenger = passengerList.get(passengerList.size() - 1);
        assertThat(testPassenger.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPassenger.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testPassenger.getBirthday()).isEqualTo(UPDATED_BIRTHDAY);
        assertThat(testPassenger.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testPassenger.getCitizenship()).isEqualTo(UPDATED_CITIZENSHIP);
        assertThat(testPassenger.getContactNumber()).isEqualTo(UPDATED_CONTACT_NUMBER);
        assertThat(testPassenger.getEmailAddress()).isEqualTo(UPDATED_EMAIL_ADDRESS);
    }

    @Test
    @Transactional
    void patchNonExistingPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();
        passenger.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passenger.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passenger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();
        passenger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passenger))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassenger() throws Exception {
        int databaseSizeBeforeUpdate = passengerRepository.findAll().size();
        passenger.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassengerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(passenger))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passenger in the database
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassenger() throws Exception {
        // Initialize the database
        passengerRepository.saveAndFlush(passenger);

        int databaseSizeBeforeDelete = passengerRepository.findAll().size();

        // Delete the passenger
        restPassengerMockMvc
            .perform(delete(ENTITY_API_URL_ID, passenger.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passenger> passengerList = passengerRepository.findAll();
        assertThat(passengerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
