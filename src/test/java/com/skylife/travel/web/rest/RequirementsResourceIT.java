package com.skylife.travel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skylife.travel.IntegrationTest;
import com.skylife.travel.domain.Requirements;
import com.skylife.travel.repository.RequirementsRepository;
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
 * Integration tests for the {@link RequirementsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RequirementsResourceIT {

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/requirements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RequirementsRepository requirementsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRequirementsMockMvc;

    private Requirements requirements;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requirements createEntity(EntityManager em) {
        Requirements requirements = new Requirements().destination(DEFAULT_DESTINATION).description(DEFAULT_DESCRIPTION);
        return requirements;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Requirements createUpdatedEntity(EntityManager em) {
        Requirements requirements = new Requirements().destination(UPDATED_DESTINATION).description(UPDATED_DESCRIPTION);
        return requirements;
    }

    @BeforeEach
    public void initTest() {
        requirements = createEntity(em);
    }

    @Test
    @Transactional
    void createRequirements() throws Exception {
        int databaseSizeBeforeCreate = requirementsRepository.findAll().size();
        // Create the Requirements
        restRequirementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requirements)))
            .andExpect(status().isCreated());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeCreate + 1);
        Requirements testRequirements = requirementsList.get(requirementsList.size() - 1);
        assertThat(testRequirements.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testRequirements.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createRequirementsWithExistingId() throws Exception {
        // Create the Requirements with an existing ID
        requirements.setId(1L);

        int databaseSizeBeforeCreate = requirementsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRequirementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requirements)))
            .andExpect(status().isBadRequest());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = requirementsRepository.findAll().size();
        // set the field null
        requirements.setDestination(null);

        // Create the Requirements, which fails.

        restRequirementsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requirements)))
            .andExpect(status().isBadRequest());

        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRequirements() throws Exception {
        // Initialize the database
        requirementsRepository.saveAndFlush(requirements);

        // Get all the requirementsList
        restRequirementsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(requirements.getId().intValue())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getRequirements() throws Exception {
        // Initialize the database
        requirementsRepository.saveAndFlush(requirements);

        // Get the requirements
        restRequirementsMockMvc
            .perform(get(ENTITY_API_URL_ID, requirements.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(requirements.getId().intValue()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRequirements() throws Exception {
        // Get the requirements
        restRequirementsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRequirements() throws Exception {
        // Initialize the database
        requirementsRepository.saveAndFlush(requirements);

        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();

        // Update the requirements
        Requirements updatedRequirements = requirementsRepository.findById(requirements.getId()).get();
        // Disconnect from session so that the updates on updatedRequirements are not directly saved in db
        em.detach(updatedRequirements);
        updatedRequirements.destination(UPDATED_DESTINATION).description(UPDATED_DESCRIPTION);

        restRequirementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRequirements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRequirements))
            )
            .andExpect(status().isOk());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
        Requirements testRequirements = requirementsList.get(requirementsList.size() - 1);
        assertThat(testRequirements.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testRequirements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingRequirements() throws Exception {
        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();
        requirements.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequirementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, requirements.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requirements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRequirements() throws Exception {
        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();
        requirements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequirementsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(requirements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRequirements() throws Exception {
        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();
        requirements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequirementsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(requirements)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRequirementsWithPatch() throws Exception {
        // Initialize the database
        requirementsRepository.saveAndFlush(requirements);

        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();

        // Update the requirements using partial update
        Requirements partialUpdatedRequirements = new Requirements();
        partialUpdatedRequirements.setId(requirements.getId());

        partialUpdatedRequirements.destination(UPDATED_DESTINATION).description(UPDATED_DESCRIPTION);

        restRequirementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequirements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequirements))
            )
            .andExpect(status().isOk());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
        Requirements testRequirements = requirementsList.get(requirementsList.size() - 1);
        assertThat(testRequirements.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testRequirements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateRequirementsWithPatch() throws Exception {
        // Initialize the database
        requirementsRepository.saveAndFlush(requirements);

        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();

        // Update the requirements using partial update
        Requirements partialUpdatedRequirements = new Requirements();
        partialUpdatedRequirements.setId(requirements.getId());

        partialUpdatedRequirements.destination(UPDATED_DESTINATION).description(UPDATED_DESCRIPTION);

        restRequirementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRequirements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRequirements))
            )
            .andExpect(status().isOk());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
        Requirements testRequirements = requirementsList.get(requirementsList.size() - 1);
        assertThat(testRequirements.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testRequirements.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingRequirements() throws Exception {
        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();
        requirements.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRequirementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, requirements.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requirements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRequirements() throws Exception {
        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();
        requirements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequirementsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(requirements))
            )
            .andExpect(status().isBadRequest());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRequirements() throws Exception {
        int databaseSizeBeforeUpdate = requirementsRepository.findAll().size();
        requirements.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRequirementsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(requirements))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Requirements in the database
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRequirements() throws Exception {
        // Initialize the database
        requirementsRepository.saveAndFlush(requirements);

        int databaseSizeBeforeDelete = requirementsRepository.findAll().size();

        // Delete the requirements
        restRequirementsMockMvc
            .perform(delete(ENTITY_API_URL_ID, requirements.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Requirements> requirementsList = requirementsRepository.findAll();
        assertThat(requirementsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
