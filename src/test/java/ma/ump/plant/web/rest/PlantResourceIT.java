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
import ma.ump.plant.domain.Family;
import ma.ump.plant.domain.Plant;
import ma.ump.plant.repository.PlantRepository;
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
 * Integration tests for the {@link PlantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlantResourceIT {

    private static final String DEFAULT_SCIENTIFIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SCIENTIFIC_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SYNONYM = "AAAAAAAAAA";
    private static final String UPDATED_SYNONYM = "BBBBBBBBBB";

    private static final String DEFAULT_LOCAL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LOCAL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENGLISH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENGLISH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VOUCHER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_VOUCHER_NUMBER = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PICTURE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PICTURE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PICTURE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PICTURE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_BOTANICAL_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_BOTANICAL_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_THERAPEUTIC_USES = "AAAAAAAAAA";
    private static final String UPDATED_THERAPEUTIC_USES = "BBBBBBBBBB";

    private static final String DEFAULT_USED_PARTS = "AAAAAAAAAA";
    private static final String UPDATED_USED_PARTS = "BBBBBBBBBB";

    private static final String DEFAULT_PREPARATION = "AAAAAAAAAA";
    private static final String UPDATED_PREPARATION = "BBBBBBBBBB";

    private static final String DEFAULT_PHARMACOLOGICAL_ACTIVITIES = "AAAAAAAAAA";
    private static final String UPDATED_PHARMACOLOGICAL_ACTIVITIES = "BBBBBBBBBB";

    private static final String DEFAULT_MAJOR_PHYTOCHEMICALS = "AAAAAAAAAA";
    private static final String UPDATED_MAJOR_PHYTOCHEMICALS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/plants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlantMockMvc;

    private Plant plant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plant createEntity(EntityManager em) {
        Plant plant = new Plant()
            .scientificName(DEFAULT_SCIENTIFIC_NAME)
            .synonym(DEFAULT_SYNONYM)
            .localName(DEFAULT_LOCAL_NAME)
            .englishName(DEFAULT_ENGLISH_NAME)
            .voucherNumber(DEFAULT_VOUCHER_NUMBER)
            .picture(DEFAULT_PICTURE)
            .pictureContentType(DEFAULT_PICTURE_CONTENT_TYPE)
            .botanicalDescription(DEFAULT_BOTANICAL_DESCRIPTION)
            .therapeuticUses(DEFAULT_THERAPEUTIC_USES)
            .usedParts(DEFAULT_USED_PARTS)
            .preparation(DEFAULT_PREPARATION)
            .pharmacologicalActivities(DEFAULT_PHARMACOLOGICAL_ACTIVITIES)
            .majorPhytochemicals(DEFAULT_MAJOR_PHYTOCHEMICALS);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        plant.setFamily(family);
        return plant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plant createUpdatedEntity(EntityManager em) {
        Plant plant = new Plant()
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .synonym(UPDATED_SYNONYM)
            .localName(UPDATED_LOCAL_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .voucherNumber(UPDATED_VOUCHER_NUMBER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .botanicalDescription(UPDATED_BOTANICAL_DESCRIPTION)
            .therapeuticUses(UPDATED_THERAPEUTIC_USES)
            .usedParts(UPDATED_USED_PARTS)
            .preparation(UPDATED_PREPARATION)
            .pharmacologicalActivities(UPDATED_PHARMACOLOGICAL_ACTIVITIES)
            .majorPhytochemicals(UPDATED_MAJOR_PHYTOCHEMICALS);
        // Add required entity
        Family family;
        if (TestUtil.findAll(em, Family.class).isEmpty()) {
            family = FamilyResourceIT.createUpdatedEntity(em);
            em.persist(family);
            em.flush();
        } else {
            family = TestUtil.findAll(em, Family.class).get(0);
        }
        plant.setFamily(family);
        return plant;
    }

    @BeforeEach
    public void initTest() {
        plant = createEntity(em);
    }

    @Test
    @Transactional
    void createPlant() throws Exception {
        int databaseSizeBeforeCreate = plantRepository.findAll().size();
        // Create the Plant
        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plant)))
            .andExpect(status().isCreated());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeCreate + 1);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getScientificName()).isEqualTo(DEFAULT_SCIENTIFIC_NAME);
        assertThat(testPlant.getSynonym()).isEqualTo(DEFAULT_SYNONYM);
        assertThat(testPlant.getLocalName()).isEqualTo(DEFAULT_LOCAL_NAME);
        assertThat(testPlant.getEnglishName()).isEqualTo(DEFAULT_ENGLISH_NAME);
        assertThat(testPlant.getVoucherNumber()).isEqualTo(DEFAULT_VOUCHER_NUMBER);
        assertThat(testPlant.getPicture()).isEqualTo(DEFAULT_PICTURE);
        assertThat(testPlant.getPictureContentType()).isEqualTo(DEFAULT_PICTURE_CONTENT_TYPE);
        assertThat(testPlant.getBotanicalDescription()).isEqualTo(DEFAULT_BOTANICAL_DESCRIPTION);
        assertThat(testPlant.getTherapeuticUses()).isEqualTo(DEFAULT_THERAPEUTIC_USES);
        assertThat(testPlant.getUsedParts()).isEqualTo(DEFAULT_USED_PARTS);
        assertThat(testPlant.getPreparation()).isEqualTo(DEFAULT_PREPARATION);
        assertThat(testPlant.getPharmacologicalActivities()).isEqualTo(DEFAULT_PHARMACOLOGICAL_ACTIVITIES);
        assertThat(testPlant.getMajorPhytochemicals()).isEqualTo(DEFAULT_MAJOR_PHYTOCHEMICALS);
    }

    @Test
    @Transactional
    void createPlantWithExistingId() throws Exception {
        // Create the Plant with an existing ID
        plant.setId(1L);

        int databaseSizeBeforeCreate = plantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plant)))
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScientificNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantRepository.findAll().size();
        // set the field null
        plant.setScientificName(null);

        // Create the Plant, which fails.

        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plant)))
            .andExpect(status().isBadRequest());

        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVoucherNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = plantRepository.findAll().size();
        // set the field null
        plant.setVoucherNumber(null);

        // Create the Plant, which fails.

        restPlantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plant)))
            .andExpect(status().isBadRequest());

        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlants() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get all the plantList
        restPlantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plant.getId().intValue())))
            .andExpect(jsonPath("$.[*].scientificName").value(hasItem(DEFAULT_SCIENTIFIC_NAME)))
            .andExpect(jsonPath("$.[*].synonym").value(hasItem(DEFAULT_SYNONYM)))
            .andExpect(jsonPath("$.[*].localName").value(hasItem(DEFAULT_LOCAL_NAME)))
            .andExpect(jsonPath("$.[*].englishName").value(hasItem(DEFAULT_ENGLISH_NAME)))
            .andExpect(jsonPath("$.[*].voucherNumber").value(hasItem(DEFAULT_VOUCHER_NUMBER)))
            .andExpect(jsonPath("$.[*].pictureContentType").value(hasItem(DEFAULT_PICTURE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].picture").value(hasItem(Base64Utils.encodeToString(DEFAULT_PICTURE))))
            .andExpect(jsonPath("$.[*].botanicalDescription").value(hasItem(DEFAULT_BOTANICAL_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].therapeuticUses").value(hasItem(DEFAULT_THERAPEUTIC_USES)))
            .andExpect(jsonPath("$.[*].usedParts").value(hasItem(DEFAULT_USED_PARTS)))
            .andExpect(jsonPath("$.[*].preparation").value(hasItem(DEFAULT_PREPARATION)))
            .andExpect(jsonPath("$.[*].pharmacologicalActivities").value(hasItem(DEFAULT_PHARMACOLOGICAL_ACTIVITIES)))
            .andExpect(jsonPath("$.[*].majorPhytochemicals").value(hasItem(DEFAULT_MAJOR_PHYTOCHEMICALS)));
    }

    @Test
    @Transactional
    void getPlant() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        // Get the plant
        restPlantMockMvc
            .perform(get(ENTITY_API_URL_ID, plant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plant.getId().intValue()))
            .andExpect(jsonPath("$.scientificName").value(DEFAULT_SCIENTIFIC_NAME))
            .andExpect(jsonPath("$.synonym").value(DEFAULT_SYNONYM))
            .andExpect(jsonPath("$.localName").value(DEFAULT_LOCAL_NAME))
            .andExpect(jsonPath("$.englishName").value(DEFAULT_ENGLISH_NAME))
            .andExpect(jsonPath("$.voucherNumber").value(DEFAULT_VOUCHER_NUMBER))
            .andExpect(jsonPath("$.pictureContentType").value(DEFAULT_PICTURE_CONTENT_TYPE))
            .andExpect(jsonPath("$.picture").value(Base64Utils.encodeToString(DEFAULT_PICTURE)))
            .andExpect(jsonPath("$.botanicalDescription").value(DEFAULT_BOTANICAL_DESCRIPTION))
            .andExpect(jsonPath("$.therapeuticUses").value(DEFAULT_THERAPEUTIC_USES))
            .andExpect(jsonPath("$.usedParts").value(DEFAULT_USED_PARTS))
            .andExpect(jsonPath("$.preparation").value(DEFAULT_PREPARATION))
            .andExpect(jsonPath("$.pharmacologicalActivities").value(DEFAULT_PHARMACOLOGICAL_ACTIVITIES))
            .andExpect(jsonPath("$.majorPhytochemicals").value(DEFAULT_MAJOR_PHYTOCHEMICALS));
    }

    @Test
    @Transactional
    void getNonExistingPlant() throws Exception {
        // Get the plant
        restPlantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPlant() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeUpdate = plantRepository.findAll().size();

        // Update the plant
        Plant updatedPlant = plantRepository.findById(plant.getId()).get();
        // Disconnect from session so that the updates on updatedPlant are not directly saved in db
        em.detach(updatedPlant);
        updatedPlant
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .synonym(UPDATED_SYNONYM)
            .localName(UPDATED_LOCAL_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .voucherNumber(UPDATED_VOUCHER_NUMBER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .botanicalDescription(UPDATED_BOTANICAL_DESCRIPTION)
            .therapeuticUses(UPDATED_THERAPEUTIC_USES)
            .usedParts(UPDATED_USED_PARTS)
            .preparation(UPDATED_PREPARATION)
            .pharmacologicalActivities(UPDATED_PHARMACOLOGICAL_ACTIVITIES)
            .majorPhytochemicals(UPDATED_MAJOR_PHYTOCHEMICALS);

        restPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPlant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPlant))
            )
            .andExpect(status().isOk());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getScientificName()).isEqualTo(UPDATED_SCIENTIFIC_NAME);
        assertThat(testPlant.getSynonym()).isEqualTo(UPDATED_SYNONYM);
        assertThat(testPlant.getLocalName()).isEqualTo(UPDATED_LOCAL_NAME);
        assertThat(testPlant.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
        assertThat(testPlant.getVoucherNumber()).isEqualTo(UPDATED_VOUCHER_NUMBER);
        assertThat(testPlant.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPlant.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testPlant.getBotanicalDescription()).isEqualTo(UPDATED_BOTANICAL_DESCRIPTION);
        assertThat(testPlant.getTherapeuticUses()).isEqualTo(UPDATED_THERAPEUTIC_USES);
        assertThat(testPlant.getUsedParts()).isEqualTo(UPDATED_USED_PARTS);
        assertThat(testPlant.getPreparation()).isEqualTo(UPDATED_PREPARATION);
        assertThat(testPlant.getPharmacologicalActivities()).isEqualTo(UPDATED_PHARMACOLOGICAL_ACTIVITIES);
        assertThat(testPlant.getMajorPhytochemicals()).isEqualTo(UPDATED_MAJOR_PHYTOCHEMICALS);
    }

    @Test
    @Transactional
    void putNonExistingPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, plant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(plant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(plant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlantWithPatch() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeUpdate = plantRepository.findAll().size();

        // Update the plant using partial update
        Plant partialUpdatedPlant = new Plant();
        partialUpdatedPlant.setId(plant.getId());

        partialUpdatedPlant
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .voucherNumber(UPDATED_VOUCHER_NUMBER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .usedParts(UPDATED_USED_PARTS)
            .preparation(UPDATED_PREPARATION)
            .majorPhytochemicals(UPDATED_MAJOR_PHYTOCHEMICALS);

        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlant))
            )
            .andExpect(status().isOk());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getScientificName()).isEqualTo(UPDATED_SCIENTIFIC_NAME);
        assertThat(testPlant.getSynonym()).isEqualTo(DEFAULT_SYNONYM);
        assertThat(testPlant.getLocalName()).isEqualTo(DEFAULT_LOCAL_NAME);
        assertThat(testPlant.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
        assertThat(testPlant.getVoucherNumber()).isEqualTo(UPDATED_VOUCHER_NUMBER);
        assertThat(testPlant.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPlant.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testPlant.getBotanicalDescription()).isEqualTo(DEFAULT_BOTANICAL_DESCRIPTION);
        assertThat(testPlant.getTherapeuticUses()).isEqualTo(DEFAULT_THERAPEUTIC_USES);
        assertThat(testPlant.getUsedParts()).isEqualTo(UPDATED_USED_PARTS);
        assertThat(testPlant.getPreparation()).isEqualTo(UPDATED_PREPARATION);
        assertThat(testPlant.getPharmacologicalActivities()).isEqualTo(DEFAULT_PHARMACOLOGICAL_ACTIVITIES);
        assertThat(testPlant.getMajorPhytochemicals()).isEqualTo(UPDATED_MAJOR_PHYTOCHEMICALS);
    }

    @Test
    @Transactional
    void fullUpdatePlantWithPatch() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeUpdate = plantRepository.findAll().size();

        // Update the plant using partial update
        Plant partialUpdatedPlant = new Plant();
        partialUpdatedPlant.setId(plant.getId());

        partialUpdatedPlant
            .scientificName(UPDATED_SCIENTIFIC_NAME)
            .synonym(UPDATED_SYNONYM)
            .localName(UPDATED_LOCAL_NAME)
            .englishName(UPDATED_ENGLISH_NAME)
            .voucherNumber(UPDATED_VOUCHER_NUMBER)
            .picture(UPDATED_PICTURE)
            .pictureContentType(UPDATED_PICTURE_CONTENT_TYPE)
            .botanicalDescription(UPDATED_BOTANICAL_DESCRIPTION)
            .therapeuticUses(UPDATED_THERAPEUTIC_USES)
            .usedParts(UPDATED_USED_PARTS)
            .preparation(UPDATED_PREPARATION)
            .pharmacologicalActivities(UPDATED_PHARMACOLOGICAL_ACTIVITIES)
            .majorPhytochemicals(UPDATED_MAJOR_PHYTOCHEMICALS);

        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPlant))
            )
            .andExpect(status().isOk());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
        Plant testPlant = plantList.get(plantList.size() - 1);
        assertThat(testPlant.getScientificName()).isEqualTo(UPDATED_SCIENTIFIC_NAME);
        assertThat(testPlant.getSynonym()).isEqualTo(UPDATED_SYNONYM);
        assertThat(testPlant.getLocalName()).isEqualTo(UPDATED_LOCAL_NAME);
        assertThat(testPlant.getEnglishName()).isEqualTo(UPDATED_ENGLISH_NAME);
        assertThat(testPlant.getVoucherNumber()).isEqualTo(UPDATED_VOUCHER_NUMBER);
        assertThat(testPlant.getPicture()).isEqualTo(UPDATED_PICTURE);
        assertThat(testPlant.getPictureContentType()).isEqualTo(UPDATED_PICTURE_CONTENT_TYPE);
        assertThat(testPlant.getBotanicalDescription()).isEqualTo(UPDATED_BOTANICAL_DESCRIPTION);
        assertThat(testPlant.getTherapeuticUses()).isEqualTo(UPDATED_THERAPEUTIC_USES);
        assertThat(testPlant.getUsedParts()).isEqualTo(UPDATED_USED_PARTS);
        assertThat(testPlant.getPreparation()).isEqualTo(UPDATED_PREPARATION);
        assertThat(testPlant.getPharmacologicalActivities()).isEqualTo(UPDATED_PHARMACOLOGICAL_ACTIVITIES);
        assertThat(testPlant.getMajorPhytochemicals()).isEqualTo(UPDATED_MAJOR_PHYTOCHEMICALS);
    }

    @Test
    @Transactional
    void patchNonExistingPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, plant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(plant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlant() throws Exception {
        int databaseSizeBeforeUpdate = plantRepository.findAll().size();
        plant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(plant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plant in the database
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlant() throws Exception {
        // Initialize the database
        plantRepository.saveAndFlush(plant);

        int databaseSizeBeforeDelete = plantRepository.findAll().size();

        // Delete the plant
        restPlantMockMvc
            .perform(delete(ENTITY_API_URL_ID, plant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Plant> plantList = plantRepository.findAll();
        assertThat(plantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
