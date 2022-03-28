package com.skylife.travel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skylife.travel.IntegrationTest;
import com.skylife.travel.domain.OHDC;
import com.skylife.travel.repository.OHDCRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link OHDCResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OHDCResourceIT {

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ohdcs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OHDCRepository oHDCRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOHDCMockMvc;

    private OHDC oHDC;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OHDC createEntity(EntityManager em) {
        OHDC oHDC = new OHDC()
            .destination(DEFAULT_DESTINATION)
            .description(DEFAULT_DESCRIPTION)
            .contactDescription(DEFAULT_CONTACT_DESCRIPTION);
        return oHDC;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OHDC createUpdatedEntity(EntityManager em) {
        OHDC oHDC = new OHDC()
            .destination(UPDATED_DESTINATION)
            .description(UPDATED_DESCRIPTION)
            .contactDescription(UPDATED_CONTACT_DESCRIPTION);
        return oHDC;
    }

    @BeforeEach
    public void initTest() {
        oHDC = createEntity(em);
    }

    @Test
    @Transactional
    void createOHDC() throws Exception {
        int databaseSizeBeforeCreate = oHDCRepository.findAll().size();
        // Create the OHDC
        restOHDCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oHDC)))
            .andExpect(status().isCreated());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeCreate + 1);
        OHDC testOHDC = oHDCList.get(oHDCList.size() - 1);
        assertThat(testOHDC.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testOHDC.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOHDC.getContactDescription()).isEqualTo(DEFAULT_CONTACT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createOHDCWithExistingId() throws Exception {
        // Create the OHDC with an existing ID
        oHDC.setId(1L);

        int databaseSizeBeforeCreate = oHDCRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOHDCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oHDC)))
            .andExpect(status().isBadRequest());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = oHDCRepository.findAll().size();
        // set the field null
        oHDC.setDestination(null);

        // Create the OHDC, which fails.

        restOHDCMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oHDC)))
            .andExpect(status().isBadRequest());

        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOHDCS() throws Exception {
        // Initialize the database
        oHDCRepository.saveAndFlush(oHDC);

        // Get all the oHDCList
        restOHDCMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(oHDC.getId().intValue())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].contactDescription").value(hasItem(DEFAULT_CONTACT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getOHDC() throws Exception {
        // Initialize the database
        oHDCRepository.saveAndFlush(oHDC);

        // Get the oHDC
        restOHDCMockMvc
            .perform(get(ENTITY_API_URL_ID, oHDC.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(oHDC.getId().intValue()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.contactDescription").value(DEFAULT_CONTACT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingOHDC() throws Exception {
        // Get the oHDC
        restOHDCMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOHDC() throws Exception {
        // Initialize the database
        oHDCRepository.saveAndFlush(oHDC);

        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();

        // Update the oHDC
        OHDC updatedOHDC = oHDCRepository.findById(oHDC.getId()).get();
        // Disconnect from session so that the updates on updatedOHDC are not directly saved in db
        em.detach(updatedOHDC);
        updatedOHDC.destination(UPDATED_DESTINATION).description(UPDATED_DESCRIPTION).contactDescription(UPDATED_CONTACT_DESCRIPTION);

        restOHDCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOHDC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOHDC))
            )
            .andExpect(status().isOk());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
        OHDC testOHDC = oHDCList.get(oHDCList.size() - 1);
        assertThat(testOHDC.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testOHDC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOHDC.getContactDescription()).isEqualTo(UPDATED_CONTACT_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingOHDC() throws Exception {
        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();
        oHDC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOHDCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, oHDC.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oHDC))
            )
            .andExpect(status().isBadRequest());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOHDC() throws Exception {
        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();
        oHDC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOHDCMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(oHDC))
            )
            .andExpect(status().isBadRequest());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOHDC() throws Exception {
        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();
        oHDC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOHDCMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(oHDC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOHDCWithPatch() throws Exception {
        // Initialize the database
        oHDCRepository.saveAndFlush(oHDC);

        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();

        // Update the oHDC using partial update
        OHDC partialUpdatedOHDC = new OHDC();
        partialUpdatedOHDC.setId(oHDC.getId());

        partialUpdatedOHDC.description(UPDATED_DESCRIPTION).contactDescription(UPDATED_CONTACT_DESCRIPTION);

        restOHDCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOHDC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOHDC))
            )
            .andExpect(status().isOk());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
        OHDC testOHDC = oHDCList.get(oHDCList.size() - 1);
        assertThat(testOHDC.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testOHDC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOHDC.getContactDescription()).isEqualTo(UPDATED_CONTACT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateOHDCWithPatch() throws Exception {
        // Initialize the database
        oHDCRepository.saveAndFlush(oHDC);

        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();

        // Update the oHDC using partial update
        OHDC partialUpdatedOHDC = new OHDC();
        partialUpdatedOHDC.setId(oHDC.getId());

        partialUpdatedOHDC
            .destination(UPDATED_DESTINATION)
            .description(UPDATED_DESCRIPTION)
            .contactDescription(UPDATED_CONTACT_DESCRIPTION);

        restOHDCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOHDC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOHDC))
            )
            .andExpect(status().isOk());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
        OHDC testOHDC = oHDCList.get(oHDCList.size() - 1);
        assertThat(testOHDC.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testOHDC.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOHDC.getContactDescription()).isEqualTo(UPDATED_CONTACT_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingOHDC() throws Exception {
        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();
        oHDC.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOHDCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, oHDC.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oHDC))
            )
            .andExpect(status().isBadRequest());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOHDC() throws Exception {
        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();
        oHDC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOHDCMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(oHDC))
            )
            .andExpect(status().isBadRequest());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOHDC() throws Exception {
        int databaseSizeBeforeUpdate = oHDCRepository.findAll().size();
        oHDC.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOHDCMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(oHDC)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OHDC in the database
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOHDC() throws Exception {
        // Initialize the database
        oHDCRepository.saveAndFlush(oHDC);

        int databaseSizeBeforeDelete = oHDCRepository.findAll().size();

        // Delete the oHDC
        restOHDCMockMvc
            .perform(delete(ENTITY_API_URL_ID, oHDC.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OHDC> oHDCList = oHDCRepository.findAll();
        assertThat(oHDCList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
