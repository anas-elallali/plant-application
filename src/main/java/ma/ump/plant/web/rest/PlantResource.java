package ma.ump.plant.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.ump.plant.domain.Plant;
import ma.ump.plant.repository.PlantRepository;
import ma.ump.plant.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ma.ump.plant.domain.Plant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlantResource {

    private final Logger log = LoggerFactory.getLogger(PlantResource.class);

    private static final String ENTITY_NAME = "plant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlantRepository plantRepository;

    public PlantResource(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    /**
     * {@code POST  /plants} : Create a new plant.
     *
     * @param plant the plant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new plant, or with status {@code 400 (Bad Request)} if the plant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/plants")
    public ResponseEntity<Plant> createPlant(@Valid @RequestBody Plant plant) throws URISyntaxException {
        log.debug("REST request to save Plant : {}", plant);
        if (plant.getId() != null) {
            throw new BadRequestAlertException("A new plant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Plant result = plantRepository.save(plant);
        return ResponseEntity
            .created(new URI("/api/plants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /plants/:id} : Updates an existing plant.
     *
     * @param id the id of the plant to save.
     * @param plant the plant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plant,
     * or with status {@code 400 (Bad Request)} if the plant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the plant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/plants/{id}")
    public ResponseEntity<Plant> updatePlant(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Plant plant)
        throws URISyntaxException {
        log.debug("REST request to update Plant : {}, {}", id, plant);
        if (plant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Plant result = plantRepository.save(plant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /plants/:id} : Partial updates given fields of an existing plant, field will ignore if it is null
     *
     * @param id the id of the plant to save.
     * @param plant the plant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated plant,
     * or with status {@code 400 (Bad Request)} if the plant is not valid,
     * or with status {@code 404 (Not Found)} if the plant is not found,
     * or with status {@code 500 (Internal Server Error)} if the plant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/plants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Plant> partialUpdatePlant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Plant plant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Plant partially : {}, {}", id, plant);
        if (plant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, plant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!plantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Plant> result = plantRepository
            .findById(plant.getId())
            .map(
                existingPlant -> {
                    if (plant.getScientificName() != null) {
                        existingPlant.setScientificName(plant.getScientificName());
                    }
                    if (plant.getSynonym() != null) {
                        existingPlant.setSynonym(plant.getSynonym());
                    }
                    if (plant.getLocalName() != null) {
                        existingPlant.setLocalName(plant.getLocalName());
                    }
                    if (plant.getEnglishName() != null) {
                        existingPlant.setEnglishName(plant.getEnglishName());
                    }
                    if (plant.getVoucherNumber() != null) {
                        existingPlant.setVoucherNumber(plant.getVoucherNumber());
                    }
                    if (plant.getPicture() != null) {
                        existingPlant.setPicture(plant.getPicture());
                    }
                    if (plant.getPictureContentType() != null) {
                        existingPlant.setPictureContentType(plant.getPictureContentType());
                    }
                    if (plant.getBotanicalDescription() != null) {
                        existingPlant.setBotanicalDescription(plant.getBotanicalDescription());
                    }
                    if (plant.getTherapeuticUses() != null) {
                        existingPlant.setTherapeuticUses(plant.getTherapeuticUses());
                    }
                    if (plant.getUsedParts() != null) {
                        existingPlant.setUsedParts(plant.getUsedParts());
                    }
                    if (plant.getPreparation() != null) {
                        existingPlant.setPreparation(plant.getPreparation());
                    }
                    if (plant.getPharmacologicalActivities() != null) {
                        existingPlant.setPharmacologicalActivities(plant.getPharmacologicalActivities());
                    }
                    if (plant.getMajorPhytochemicals() != null) {
                        existingPlant.setMajorPhytochemicals(plant.getMajorPhytochemicals());
                    }

                    return existingPlant;
                }
            )
            .map(plantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, plant.getId().toString())
        );
    }

    /**
     * {@code GET  /plants} : get all the plants.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of plants in body.
     */
    @GetMapping("/public/plants")
    public ResponseEntity<List<Plant>> getAllPlants(Pageable pageable) {
        log.debug("REST request to get a page of Plants");
        Page<Plant> page = plantRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/public/plants/family/{id}")
    public ResponseEntity<List<Plant>> getAllPlantsByFamily(
        @PathVariable Long id,
        @RequestParam(required=false) String searchKey,
        @RequestParam(required=false) String value,
        Pageable pageable) {
        log.debug("REST request to get a page of Plants");

        Page<Plant> page;

        switch (searchKey){
            case "localName":

                page = plantRepository.findByFamilyIdAndLocalNameContainingIgnoreCase(id, value, pageable);
                break;
            case "voucherNumber":
                page = plantRepository.findByFamilyIdAndVoucherNumberContainingIgnoreCase(id, value, pageable);
                break;
            case "englishName":
                page = plantRepository.findByFamilyIdAndEnglishNameContainingIgnoreCase(id, value, pageable);
                break;
            case "scientificName":
                page = plantRepository.findByFamilyIdAndScientificNameContainingIgnoreCase(id, value, pageable);
                break;
            default:
                page = plantRepository.findByFamilyId(id, pageable);
        }

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plants/:id} : get the "id" plant.
     *
     * @param id the id of the plant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the plant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/public/plants/{id}")
    public ResponseEntity<Plant> getPlant(@PathVariable Long id) {
        log.debug("REST request to get Plant : {}", id);
        Optional<Plant> plant = plantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(plant);
    }

    /**
     * {@code DELETE  /plants/:id} : delete the "id" plant.
     *
     * @param id the id of the plant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/plants/{id}")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        log.debug("REST request to delete Plant : {}", id);
        plantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
