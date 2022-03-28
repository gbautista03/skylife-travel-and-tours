package com.skylife.travel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skylife.travel.IntegrationTest;
import com.skylife.travel.domain.PackageTour;
import com.skylife.travel.repository.PackageTourRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PackageTourResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PackageTourResourceIT {

    private static final Integer DEFAULT_DAYS = 1;
    private static final Integer UPDATED_DAYS = 2;

    private static final Integer DEFAULT_NIGHTS = 1;
    private static final Integer UPDATED_NIGHTS = 2;

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_TOUR_CODE = "AAAAAAAAAA";
    private static final String UPDATED_TOUR_CODE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_HOTEL = "AAAAAAAAAA";
    private static final String UPDATED_HOTEL = "BBBBBBBBBB";

    private static final String DEFAULT_ROOM_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_TYPE = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMBER_OF_GUEST = 1;
    private static final Integer UPDATED_NUMBER_OF_GUEST = 2;

    private static final String ENTITY_API_URL = "/api/package-tours";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PackageTourRepository packageTourRepository;

    @Mock
    private PackageTourRepository packageTourRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackageTourMockMvc;

    private PackageTour packageTour;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageTour createEntity(EntityManager em) {
        PackageTour packageTour = new PackageTour()
            .days(DEFAULT_DAYS)
            .nights(DEFAULT_NIGHTS)
            .destination(DEFAULT_DESTINATION)
            .tourCode(DEFAULT_TOUR_CODE)
            .date(DEFAULT_DATE)
            .hotel(DEFAULT_HOTEL)
            .roomType(DEFAULT_ROOM_TYPE)
            .numberOfGuest(DEFAULT_NUMBER_OF_GUEST);
        return packageTour;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageTour createUpdatedEntity(EntityManager em) {
        PackageTour packageTour = new PackageTour()
            .days(UPDATED_DAYS)
            .nights(UPDATED_NIGHTS)
            .destination(UPDATED_DESTINATION)
            .tourCode(UPDATED_TOUR_CODE)
            .date(UPDATED_DATE)
            .hotel(UPDATED_HOTEL)
            .roomType(UPDATED_ROOM_TYPE)
            .numberOfGuest(UPDATED_NUMBER_OF_GUEST);
        return packageTour;
    }

    @BeforeEach
    public void initTest() {
        packageTour = createEntity(em);
    }

    @Test
    @Transactional
    void createPackageTour() throws Exception {
        int databaseSizeBeforeCreate = packageTourRepository.findAll().size();
        // Create the PackageTour
        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isCreated());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeCreate + 1);
        PackageTour testPackageTour = packageTourList.get(packageTourList.size() - 1);
        assertThat(testPackageTour.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testPackageTour.getNights()).isEqualTo(DEFAULT_NIGHTS);
        assertThat(testPackageTour.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testPackageTour.getTourCode()).isEqualTo(DEFAULT_TOUR_CODE);
        assertThat(testPackageTour.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPackageTour.getHotel()).isEqualTo(DEFAULT_HOTEL);
        assertThat(testPackageTour.getRoomType()).isEqualTo(DEFAULT_ROOM_TYPE);
        assertThat(testPackageTour.getNumberOfGuest()).isEqualTo(DEFAULT_NUMBER_OF_GUEST);
    }

    @Test
    @Transactional
    void createPackageTourWithExistingId() throws Exception {
        // Create the PackageTour with an existing ID
        packageTour.setId(1L);

        int databaseSizeBeforeCreate = packageTourRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setDays(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNightsIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setNights(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setDestination(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTourCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setTourCode(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setDate(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHotelIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setHotel(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoomTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setRoomType(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumberOfGuestIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageTourRepository.findAll().size();
        // set the field null
        packageTour.setNumberOfGuest(null);

        // Create the PackageTour, which fails.

        restPackageTourMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isBadRequest());

        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPackageTours() throws Exception {
        // Initialize the database
        packageTourRepository.saveAndFlush(packageTour);

        // Get all the packageTourList
        restPackageTourMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageTour.getId().intValue())))
            .andExpect(jsonPath("$.[*].days").value(hasItem(DEFAULT_DAYS)))
            .andExpect(jsonPath("$.[*].nights").value(hasItem(DEFAULT_NIGHTS)))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].tourCode").value(hasItem(DEFAULT_TOUR_CODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].hotel").value(hasItem(DEFAULT_HOTEL)))
            .andExpect(jsonPath("$.[*].roomType").value(hasItem(DEFAULT_ROOM_TYPE)))
            .andExpect(jsonPath("$.[*].numberOfGuest").value(hasItem(DEFAULT_NUMBER_OF_GUEST)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPackageToursWithEagerRelationshipsIsEnabled() throws Exception {
        when(packageTourRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPackageTourMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(packageTourRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPackageToursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(packageTourRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPackageTourMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(packageTourRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPackageTour() throws Exception {
        // Initialize the database
        packageTourRepository.saveAndFlush(packageTour);

        // Get the packageTour
        restPackageTourMockMvc
            .perform(get(ENTITY_API_URL_ID, packageTour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packageTour.getId().intValue()))
            .andExpect(jsonPath("$.days").value(DEFAULT_DAYS))
            .andExpect(jsonPath("$.nights").value(DEFAULT_NIGHTS))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.tourCode").value(DEFAULT_TOUR_CODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.hotel").value(DEFAULT_HOTEL))
            .andExpect(jsonPath("$.roomType").value(DEFAULT_ROOM_TYPE))
            .andExpect(jsonPath("$.numberOfGuest").value(DEFAULT_NUMBER_OF_GUEST));
    }

    @Test
    @Transactional
    void getNonExistingPackageTour() throws Exception {
        // Get the packageTour
        restPackageTourMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPackageTour() throws Exception {
        // Initialize the database
        packageTourRepository.saveAndFlush(packageTour);

        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();

        // Update the packageTour
        PackageTour updatedPackageTour = packageTourRepository.findById(packageTour.getId()).get();
        // Disconnect from session so that the updates on updatedPackageTour are not directly saved in db
        em.detach(updatedPackageTour);
        updatedPackageTour
            .days(UPDATED_DAYS)
            .nights(UPDATED_NIGHTS)
            .destination(UPDATED_DESTINATION)
            .tourCode(UPDATED_TOUR_CODE)
            .date(UPDATED_DATE)
            .hotel(UPDATED_HOTEL)
            .roomType(UPDATED_ROOM_TYPE)
            .numberOfGuest(UPDATED_NUMBER_OF_GUEST);

        restPackageTourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPackageTour.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPackageTour))
            )
            .andExpect(status().isOk());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
        PackageTour testPackageTour = packageTourList.get(packageTourList.size() - 1);
        assertThat(testPackageTour.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testPackageTour.getNights()).isEqualTo(UPDATED_NIGHTS);
        assertThat(testPackageTour.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testPackageTour.getTourCode()).isEqualTo(UPDATED_TOUR_CODE);
        assertThat(testPackageTour.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPackageTour.getHotel()).isEqualTo(UPDATED_HOTEL);
        assertThat(testPackageTour.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testPackageTour.getNumberOfGuest()).isEqualTo(UPDATED_NUMBER_OF_GUEST);
    }

    @Test
    @Transactional
    void putNonExistingPackageTour() throws Exception {
        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();
        packageTour.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageTourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packageTour.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageTour))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPackageTour() throws Exception {
        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();
        packageTour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageTourMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageTour))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPackageTour() throws Exception {
        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();
        packageTour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageTourMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(packageTour)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackageTourWithPatch() throws Exception {
        // Initialize the database
        packageTourRepository.saveAndFlush(packageTour);

        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();

        // Update the packageTour using partial update
        PackageTour partialUpdatedPackageTour = new PackageTour();
        partialUpdatedPackageTour.setId(packageTour.getId());

        partialUpdatedPackageTour.nights(UPDATED_NIGHTS).destination(UPDATED_DESTINATION).tourCode(UPDATED_TOUR_CODE).hotel(UPDATED_HOTEL);

        restPackageTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackageTour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackageTour))
            )
            .andExpect(status().isOk());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
        PackageTour testPackageTour = packageTourList.get(packageTourList.size() - 1);
        assertThat(testPackageTour.getDays()).isEqualTo(DEFAULT_DAYS);
        assertThat(testPackageTour.getNights()).isEqualTo(UPDATED_NIGHTS);
        assertThat(testPackageTour.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testPackageTour.getTourCode()).isEqualTo(UPDATED_TOUR_CODE);
        assertThat(testPackageTour.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPackageTour.getHotel()).isEqualTo(UPDATED_HOTEL);
        assertThat(testPackageTour.getRoomType()).isEqualTo(DEFAULT_ROOM_TYPE);
        assertThat(testPackageTour.getNumberOfGuest()).isEqualTo(DEFAULT_NUMBER_OF_GUEST);
    }

    @Test
    @Transactional
    void fullUpdatePackageTourWithPatch() throws Exception {
        // Initialize the database
        packageTourRepository.saveAndFlush(packageTour);

        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();

        // Update the packageTour using partial update
        PackageTour partialUpdatedPackageTour = new PackageTour();
        partialUpdatedPackageTour.setId(packageTour.getId());

        partialUpdatedPackageTour
            .days(UPDATED_DAYS)
            .nights(UPDATED_NIGHTS)
            .destination(UPDATED_DESTINATION)
            .tourCode(UPDATED_TOUR_CODE)
            .date(UPDATED_DATE)
            .hotel(UPDATED_HOTEL)
            .roomType(UPDATED_ROOM_TYPE)
            .numberOfGuest(UPDATED_NUMBER_OF_GUEST);

        restPackageTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackageTour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackageTour))
            )
            .andExpect(status().isOk());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
        PackageTour testPackageTour = packageTourList.get(packageTourList.size() - 1);
        assertThat(testPackageTour.getDays()).isEqualTo(UPDATED_DAYS);
        assertThat(testPackageTour.getNights()).isEqualTo(UPDATED_NIGHTS);
        assertThat(testPackageTour.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testPackageTour.getTourCode()).isEqualTo(UPDATED_TOUR_CODE);
        assertThat(testPackageTour.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPackageTour.getHotel()).isEqualTo(UPDATED_HOTEL);
        assertThat(testPackageTour.getRoomType()).isEqualTo(UPDATED_ROOM_TYPE);
        assertThat(testPackageTour.getNumberOfGuest()).isEqualTo(UPDATED_NUMBER_OF_GUEST);
    }

    @Test
    @Transactional
    void patchNonExistingPackageTour() throws Exception {
        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();
        packageTour.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packageTour.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packageTour))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPackageTour() throws Exception {
        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();
        packageTour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageTourMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packageTour))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPackageTour() throws Exception {
        int databaseSizeBeforeUpdate = packageTourRepository.findAll().size();
        packageTour.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageTourMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(packageTour))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PackageTour in the database
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePackageTour() throws Exception {
        // Initialize the database
        packageTourRepository.saveAndFlush(packageTour);

        int databaseSizeBeforeDelete = packageTourRepository.findAll().size();

        // Delete the packageTour
        restPackageTourMockMvc
            .perform(delete(ENTITY_API_URL_ID, packageTour.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PackageTour> packageTourList = packageTourRepository.findAll();
        assertThat(packageTourList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
