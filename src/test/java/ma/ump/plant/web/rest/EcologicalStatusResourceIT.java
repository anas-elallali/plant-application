package ma.ump.plant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ma.ump.plant.IntegrationTest;
import ma.ump.plant.domain.EcologicalStatus;
import ma.ump.plant.domain.enumeration.EcologicalStatusType;
import ma.ump.plant.repository.EcologicalStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link EcologicalStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EcologicalStatusResourceIT {

    private static final String DEFAULT_NAME = EcologicalStatusType.Spontaneous.toString();
    private static final String UPDATED_NAME = EcologicalStatusType.Cultivated.toString();

    private static final String ENTITY_API_URL = "/api/ecological-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EcologicalStatusRepository ecologicalStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEcologicalStatusMockMvc;

    private EcologicalStatus ecologicalStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EcologicalStatus createEntity(EntityManager em) {
        EcologicalStatus ecologicalStatus = new EcologicalStatus().name(DEFAULT_NAME.toString());
        return ecologicalStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EcologicalStatus createUpdatedEntity(EntityManager em) {
        EcologicalStatus ecologicalStatus = new EcologicalStatus().name(UPDATED_NAME.toString());
        return ecologicalStatus;
    }

    @BeforeEach
    public void initTest() {
        ecologicalStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createEcologicalStatus() throws Exception {
        int databaseSizeBeforeCreate = ecologicalStatusRepository.findAll().size();
        // Create the EcologicalStatus
        restEcologicalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isCreated());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeCreate + 1);
        EcologicalStatus testEcologicalStatus = ecologicalStatusList.get(ecologicalStatusList.size() - 1);
        assertThat(testEcologicalStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createEcologicalStatusWithExistingId() throws Exception {
        // Create the EcologicalStatus with an existing ID
        ecologicalStatus.setId(1L);

        int databaseSizeBeforeCreate = ecologicalStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEcologicalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ecologicalStatusRepository.findAll().size();
        // set the field null
        ecologicalStatus.setName(null);

        // Create the EcologicalStatus, which fails.

        restEcologicalStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isBadRequest());

        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEcologicalStatuses() throws Exception {
        // Initialize the database
        ecologicalStatusRepository.saveAndFlush(ecologicalStatus);

        // Get all the ecologicalStatusList
        restEcologicalStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ecologicalStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    void getEcologicalStatus() throws Exception {
        // Initialize the database
        ecologicalStatusRepository.saveAndFlush(ecologicalStatus);

        // Get the ecologicalStatus
        restEcologicalStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, ecologicalStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ecologicalStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEcologicalStatus() throws Exception {
        // Get the ecologicalStatus
        restEcologicalStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEcologicalStatus() throws Exception {
        // Initialize the database
        ecologicalStatusRepository.saveAndFlush(ecologicalStatus);

        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();

        // Update the ecologicalStatus
        EcologicalStatus updatedEcologicalStatus = ecologicalStatusRepository.findById(ecologicalStatus.getId()).get();
        // Disconnect from session so that the updates on updatedEcologicalStatus are not directly saved in db
        em.detach(updatedEcologicalStatus);
        updatedEcologicalStatus.name(UPDATED_NAME.toString());

        restEcologicalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEcologicalStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEcologicalStatus))
            )
            .andExpect(status().isOk());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
        EcologicalStatus testEcologicalStatus = ecologicalStatusList.get(ecologicalStatusList.size() - 1);
        assertThat(testEcologicalStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingEcologicalStatus() throws Exception {
        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();
        ecologicalStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcologicalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ecologicalStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEcologicalStatus() throws Exception {
        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();
        ecologicalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcologicalStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEcologicalStatus() throws Exception {
        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();
        ecologicalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcologicalStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEcologicalStatusWithPatch() throws Exception {
        // Initialize the database
        ecologicalStatusRepository.saveAndFlush(ecologicalStatus);

        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();

        // Update the ecologicalStatus using partial update
        EcologicalStatus partialUpdatedEcologicalStatus = new EcologicalStatus();
        partialUpdatedEcologicalStatus.setId(ecologicalStatus.getId());

        partialUpdatedEcologicalStatus.name(UPDATED_NAME.toString());

        restEcologicalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEcologicalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEcologicalStatus))
            )
            .andExpect(status().isOk());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
        EcologicalStatus testEcologicalStatus = ecologicalStatusList.get(ecologicalStatusList.size() - 1);
        assertThat(testEcologicalStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateEcologicalStatusWithPatch() throws Exception {
        // Initialize the database
        ecologicalStatusRepository.saveAndFlush(ecologicalStatus);

        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();

        // Update the ecologicalStatus using partial update
        EcologicalStatus partialUpdatedEcologicalStatus = new EcologicalStatus();
        partialUpdatedEcologicalStatus.setId(ecologicalStatus.getId());

        partialUpdatedEcologicalStatus.name(UPDATED_NAME.toString());

        restEcologicalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEcologicalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEcologicalStatus))
            )
            .andExpect(status().isOk());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
        EcologicalStatus testEcologicalStatus = ecologicalStatusList.get(ecologicalStatusList.size() - 1);
        assertThat(testEcologicalStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingEcologicalStatus() throws Exception {
        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();
        ecologicalStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEcologicalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ecologicalStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEcologicalStatus() throws Exception {
        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();
        ecologicalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcologicalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEcologicalStatus() throws Exception {
        int databaseSizeBeforeUpdate = ecologicalStatusRepository.findAll().size();
        ecologicalStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEcologicalStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ecologicalStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EcologicalStatus in the database
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEcologicalStatus() throws Exception {
        // Initialize the database
        ecologicalStatusRepository.saveAndFlush(ecologicalStatus);

        int databaseSizeBeforeDelete = ecologicalStatusRepository.findAll().size();

        // Delete the ecologicalStatus
        restEcologicalStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, ecologicalStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EcologicalStatus> ecologicalStatusList = ecologicalStatusRepository.findAll();
        assertThat(ecologicalStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
