package ma.ump.plant.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ma.ump.plant.domain.EcologicalStatus;
import ma.ump.plant.repository.EcologicalStatusRepository;
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
 * REST controller for managing {@link ma.ump.plant.domain.EcologicalStatus}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EcologicalStatusResource {

    private final Logger log = LoggerFactory.getLogger(EcologicalStatusResource.class);

    private static final String ENTITY_NAME = "ecologicalStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EcologicalStatusRepository ecologicalStatusRepository;

    public EcologicalStatusResource(EcologicalStatusRepository ecologicalStatusRepository) {
        this.ecologicalStatusRepository = ecologicalStatusRepository;
    }

    /**
     * {@code POST  /ecological-statuses} : Create a new ecologicalStatus.
     *
     * @param ecologicalStatus the ecologicalStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ecologicalStatus, or with status {@code 400 (Bad Request)} if the ecologicalStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ecological-statuses")
    public ResponseEntity<EcologicalStatus> createEcologicalStatus(@Valid @RequestBody EcologicalStatus ecologicalStatus)
        throws URISyntaxException {
        log.debug("REST request to save EcologicalStatus : {}", ecologicalStatus);
        if (ecologicalStatus.getId() != null) {
            throw new BadRequestAlertException("A new ecologicalStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EcologicalStatus result = ecologicalStatusRepository.save(ecologicalStatus);
        return ResponseEntity
            .created(new URI("/api/ecological-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ecological-statuses/:id} : Updates an existing ecologicalStatus.
     *
     * @param id the id of the ecologicalStatus to save.
     * @param ecologicalStatus the ecologicalStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ecologicalStatus,
     * or with status {@code 400 (Bad Request)} if the ecologicalStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ecologicalStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ecological-statuses/{id}")
    public ResponseEntity<EcologicalStatus> updateEcologicalStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EcologicalStatus ecologicalStatus
    ) throws URISyntaxException {
        log.debug("REST request to update EcologicalStatus : {}, {}", id, ecologicalStatus);
        if (ecologicalStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ecologicalStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ecologicalStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EcologicalStatus result = ecologicalStatusRepository.save(ecologicalStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ecologicalStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ecological-statuses/:id} : Partial updates given fields of an existing ecologicalStatus, field will ignore if it is null
     *
     * @param id the id of the ecologicalStatus to save.
     * @param ecologicalStatus the ecologicalStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ecologicalStatus,
     * or with status {@code 400 (Bad Request)} if the ecologicalStatus is not valid,
     * or with status {@code 404 (Not Found)} if the ecologicalStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the ecologicalStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ecological-statuses/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<EcologicalStatus> partialUpdateEcologicalStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EcologicalStatus ecologicalStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update EcologicalStatus partially : {}, {}", id, ecologicalStatus);
        if (ecologicalStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ecologicalStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ecologicalStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EcologicalStatus> result = ecologicalStatusRepository
            .findById(ecologicalStatus.getId())
            .map(
                existingEcologicalStatus -> {
                    if (ecologicalStatus.getName() != null) {
                        existingEcologicalStatus.setName(ecologicalStatus.getName());
                    }

                    return existingEcologicalStatus;
                }
            )
            .map(ecologicalStatusRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ecologicalStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /ecological-statuses} : get all the ecologicalStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ecologicalStatuses in body.
     */
    @GetMapping("/ecological-statuses")
    public ResponseEntity<List<EcologicalStatus>> getAllEcologicalStatuses(Pageable pageable) {
        log.debug("REST request to get a page of EcologicalStatuses");
        Page<EcologicalStatus> page = ecologicalStatusRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ecological-statuses/:id} : get the "id" ecologicalStatus.
     *
     * @param id the id of the ecologicalStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ecologicalStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ecological-statuses/{id}")
    public ResponseEntity<EcologicalStatus> getEcologicalStatus(@PathVariable Long id) {
        log.debug("REST request to get EcologicalStatus : {}", id);
        Optional<EcologicalStatus> ecologicalStatus = ecologicalStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ecologicalStatus);
    }

    /**
     * {@code DELETE  /ecological-statuses/:id} : delete the "id" ecologicalStatus.
     *
     * @param id the id of the ecologicalStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ecological-statuses/{id}")
    public ResponseEntity<Void> deleteEcologicalStatus(@PathVariable Long id) {
        log.debug("REST request to delete EcologicalStatus : {}", id);
        ecologicalStatusRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
