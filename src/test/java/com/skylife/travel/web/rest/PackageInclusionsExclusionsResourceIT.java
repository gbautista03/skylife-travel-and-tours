package com.skylife.travel.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.skylife.travel.IntegrationTest;
import com.skylife.travel.domain.PackageInclusionsExclusions;
import com.skylife.travel.repository.PackageInclusionsExclusionsRepository;
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
 * Integration tests for the {@link PackageInclusionsExclusionsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PackageInclusionsExclusionsResourceIT {

    private static final String DEFAULT_DESTINATION = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUSIONS = "AAAAAAAAAA";
    private static final String UPDATED_INCLUSIONS = "BBBBBBBBBB";

    private static final String DEFAULT_EXCLUSIONS = "AAAAAAAAAA";
    private static final String UPDATED_EXCLUSIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/package-inclusions-exclusions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PackageInclusionsExclusionsRepository packageInclusionsExclusionsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPackageInclusionsExclusionsMockMvc;

    private PackageInclusionsExclusions packageInclusionsExclusions;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageInclusionsExclusions createEntity(EntityManager em) {
        PackageInclusionsExclusions packageInclusionsExclusions = new PackageInclusionsExclusions()
            .destination(DEFAULT_DESTINATION)
            .inclusions(DEFAULT_INCLUSIONS)
            .exclusions(DEFAULT_EXCLUSIONS);
        return packageInclusionsExclusions;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PackageInclusionsExclusions createUpdatedEntity(EntityManager em) {
        PackageInclusionsExclusions packageInclusionsExclusions = new PackageInclusionsExclusions()
            .destination(UPDATED_DESTINATION)
            .inclusions(UPDATED_INCLUSIONS)
            .exclusions(UPDATED_EXCLUSIONS);
        return packageInclusionsExclusions;
    }

    @BeforeEach
    public void initTest() {
        packageInclusionsExclusions = createEntity(em);
    }

    @Test
    @Transactional
    void createPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeCreate = packageInclusionsExclusionsRepository.findAll().size();
        // Create the PackageInclusionsExclusions
        restPackageInclusionsExclusionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isCreated());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeCreate + 1);
        PackageInclusionsExclusions testPackageInclusionsExclusions = packageInclusionsExclusionsList.get(
            packageInclusionsExclusionsList.size() - 1
        );
        assertThat(testPackageInclusionsExclusions.getDestination()).isEqualTo(DEFAULT_DESTINATION);
        assertThat(testPackageInclusionsExclusions.getInclusions()).isEqualTo(DEFAULT_INCLUSIONS);
        assertThat(testPackageInclusionsExclusions.getExclusions()).isEqualTo(DEFAULT_EXCLUSIONS);
    }

    @Test
    @Transactional
    void createPackageInclusionsExclusionsWithExistingId() throws Exception {
        // Create the PackageInclusionsExclusions with an existing ID
        packageInclusionsExclusions.setId(1L);

        int databaseSizeBeforeCreate = packageInclusionsExclusionsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPackageInclusionsExclusionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDestinationIsRequired() throws Exception {
        int databaseSizeBeforeTest = packageInclusionsExclusionsRepository.findAll().size();
        // set the field null
        packageInclusionsExclusions.setDestination(null);

        // Create the PackageInclusionsExclusions, which fails.

        restPackageInclusionsExclusionsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isBadRequest());

        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPackageInclusionsExclusions() throws Exception {
        // Initialize the database
        packageInclusionsExclusionsRepository.saveAndFlush(packageInclusionsExclusions);

        // Get all the packageInclusionsExclusionsList
        restPackageInclusionsExclusionsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(packageInclusionsExclusions.getId().intValue())))
            .andExpect(jsonPath("$.[*].destination").value(hasItem(DEFAULT_DESTINATION)))
            .andExpect(jsonPath("$.[*].inclusions").value(hasItem(DEFAULT_INCLUSIONS.toString())))
            .andExpect(jsonPath("$.[*].exclusions").value(hasItem(DEFAULT_EXCLUSIONS.toString())));
    }

    @Test
    @Transactional
    void getPackageInclusionsExclusions() throws Exception {
        // Initialize the database
        packageInclusionsExclusionsRepository.saveAndFlush(packageInclusionsExclusions);

        // Get the packageInclusionsExclusions
        restPackageInclusionsExclusionsMockMvc
            .perform(get(ENTITY_API_URL_ID, packageInclusionsExclusions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(packageInclusionsExclusions.getId().intValue()))
            .andExpect(jsonPath("$.destination").value(DEFAULT_DESTINATION))
            .andExpect(jsonPath("$.inclusions").value(DEFAULT_INCLUSIONS.toString()))
            .andExpect(jsonPath("$.exclusions").value(DEFAULT_EXCLUSIONS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPackageInclusionsExclusions() throws Exception {
        // Get the packageInclusionsExclusions
        restPackageInclusionsExclusionsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPackageInclusionsExclusions() throws Exception {
        // Initialize the database
        packageInclusionsExclusionsRepository.saveAndFlush(packageInclusionsExclusions);

        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();

        // Update the packageInclusionsExclusions
        PackageInclusionsExclusions updatedPackageInclusionsExclusions = packageInclusionsExclusionsRepository
            .findById(packageInclusionsExclusions.getId())
            .get();
        // Disconnect from session so that the updates on updatedPackageInclusionsExclusions are not directly saved in db
        em.detach(updatedPackageInclusionsExclusions);
        updatedPackageInclusionsExclusions.destination(UPDATED_DESTINATION).inclusions(UPDATED_INCLUSIONS).exclusions(UPDATED_EXCLUSIONS);

        restPackageInclusionsExclusionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPackageInclusionsExclusions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPackageInclusionsExclusions))
            )
            .andExpect(status().isOk());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
        PackageInclusionsExclusions testPackageInclusionsExclusions = packageInclusionsExclusionsList.get(
            packageInclusionsExclusionsList.size() - 1
        );
        assertThat(testPackageInclusionsExclusions.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testPackageInclusionsExclusions.getInclusions()).isEqualTo(UPDATED_INCLUSIONS);
        assertThat(testPackageInclusionsExclusions.getExclusions()).isEqualTo(UPDATED_EXCLUSIONS);
    }

    @Test
    @Transactional
    void putNonExistingPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();
        packageInclusionsExclusions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageInclusionsExclusionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, packageInclusionsExclusions.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();
        packageInclusionsExclusions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageInclusionsExclusionsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();
        packageInclusionsExclusions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageInclusionsExclusionsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePackageInclusionsExclusionsWithPatch() throws Exception {
        // Initialize the database
        packageInclusionsExclusionsRepository.saveAndFlush(packageInclusionsExclusions);

        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();

        // Update the packageInclusionsExclusions using partial update
        PackageInclusionsExclusions partialUpdatedPackageInclusionsExclusions = new PackageInclusionsExclusions();
        partialUpdatedPackageInclusionsExclusions.setId(packageInclusionsExclusions.getId());

        partialUpdatedPackageInclusionsExclusions.destination(UPDATED_DESTINATION);

        restPackageInclusionsExclusionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackageInclusionsExclusions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackageInclusionsExclusions))
            )
            .andExpect(status().isOk());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
        PackageInclusionsExclusions testPackageInclusionsExclusions = packageInclusionsExclusionsList.get(
            packageInclusionsExclusionsList.size() - 1
        );
        assertThat(testPackageInclusionsExclusions.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testPackageInclusionsExclusions.getInclusions()).isEqualTo(DEFAULT_INCLUSIONS);
        assertThat(testPackageInclusionsExclusions.getExclusions()).isEqualTo(DEFAULT_EXCLUSIONS);
    }

    @Test
    @Transactional
    void fullUpdatePackageInclusionsExclusionsWithPatch() throws Exception {
        // Initialize the database
        packageInclusionsExclusionsRepository.saveAndFlush(packageInclusionsExclusions);

        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();

        // Update the packageInclusionsExclusions using partial update
        PackageInclusionsExclusions partialUpdatedPackageInclusionsExclusions = new PackageInclusionsExclusions();
        partialUpdatedPackageInclusionsExclusions.setId(packageInclusionsExclusions.getId());

        partialUpdatedPackageInclusionsExclusions
            .destination(UPDATED_DESTINATION)
            .inclusions(UPDATED_INCLUSIONS)
            .exclusions(UPDATED_EXCLUSIONS);

        restPackageInclusionsExclusionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPackageInclusionsExclusions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPackageInclusionsExclusions))
            )
            .andExpect(status().isOk());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
        PackageInclusionsExclusions testPackageInclusionsExclusions = packageInclusionsExclusionsList.get(
            packageInclusionsExclusionsList.size() - 1
        );
        assertThat(testPackageInclusionsExclusions.getDestination()).isEqualTo(UPDATED_DESTINATION);
        assertThat(testPackageInclusionsExclusions.getInclusions()).isEqualTo(UPDATED_INCLUSIONS);
        assertThat(testPackageInclusionsExclusions.getExclusions()).isEqualTo(UPDATED_EXCLUSIONS);
    }

    @Test
    @Transactional
    void patchNonExistingPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();
        packageInclusionsExclusions.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPackageInclusionsExclusionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, packageInclusionsExclusions.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();
        packageInclusionsExclusions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageInclusionsExclusionsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isBadRequest());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPackageInclusionsExclusions() throws Exception {
        int databaseSizeBeforeUpdate = packageInclusionsExclusionsRepository.findAll().size();
        packageInclusionsExclusions.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPackageInclusionsExclusionsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(packageInclusionsExclusions))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PackageInclusionsExclusions in the database
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePackageInclusionsExclusions() throws Exception {
        // Initialize the database
        packageInclusionsExclusionsRepository.saveAndFlush(packageInclusionsExclusions);

        int databaseSizeBeforeDelete = packageInclusionsExclusionsRepository.findAll().size();

        // Delete the packageInclusionsExclusions
        restPackageInclusionsExclusionsMockMvc
            .perform(delete(ENTITY_API_URL_ID, packageInclusionsExclusions.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PackageInclusionsExclusions> packageInclusionsExclusionsList = packageInclusionsExclusionsRepository.findAll();
        assertThat(packageInclusionsExclusionsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
