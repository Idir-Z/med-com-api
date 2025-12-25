package com.zidir.medcom.web.rest;

import com.zidir.medcom.domain.User;
import com.zidir.medcom.repository.PharmacyRepository;
import com.zidir.medcom.repository.UserRepository;
import com.zidir.medcom.security.SecurityUtils;
import com.zidir.medcom.service.PharmacyService;
import com.zidir.medcom.service.dto.PharmacyDTO;
import com.zidir.medcom.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.zidir.medcom.domain.Pharmacy}.
 */
@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyResource {

    private static final Logger LOG = LoggerFactory.getLogger(PharmacyResource.class);

    private static final String ENTITY_NAME = "pharmacy";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PharmacyService pharmacyService;

    private final PharmacyRepository pharmacyRepository;

    private final UserRepository userRepository;

    public PharmacyResource(PharmacyService pharmacyService, PharmacyRepository pharmacyRepository, UserRepository userRepository) {
        this.pharmacyService = pharmacyService;
        this.pharmacyRepository = pharmacyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Get the current authenticated user.
     *
     * @return the current user
     * @throws BadRequestAlertException if user is not authenticated or not found
     */
    private User getCurrentUser() {
        String login = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("User not authenticated", ENTITY_NAME, "notauthenticated"));

        return userRepository
            .findOneByLogin(login)
            .orElseThrow(() -> new BadRequestAlertException("User not found", ENTITY_NAME, "usernotfound"));
    }

    /**
     * Check if the current user can access a pharmacy.
     *
     * @param pharmacyId the pharmacy id to check
     * @throws AccessDeniedException if user cannot access the pharmacy
     */
    private void checkPharmacyAccess(Long pharmacyId) {
        User currentUser = getCurrentUser();

        if (currentUser.getPharmacy() == null) {
            throw new BadRequestAlertException("User has no pharmacy", ENTITY_NAME, "nopharmacy");
        }

        if (!currentUser.getPharmacy().getId().equals(pharmacyId)) {
            throw new AccessDeniedException("User can only access their own pharmacy");
        }
    }

    /**
     * {@code POST  /pharmacies} : Create a new pharmacy.
     * DISABLED: Pharmacy creation is handled through /api/register-pharmacy endpoint
     *
     * @param pharmacyDTO the pharmacyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pharmacyDTO, or with status {@code 400 (Bad Request)} if the pharmacy has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    /*
    @PostMapping("")
    public ResponseEntity<PharmacyDTO> createPharmacy(@RequestBody PharmacyDTO pharmacyDTO) throws URISyntaxException {
        LOG.debug("REST request to save Pharmacy : {}", pharmacyDTO);
        if (pharmacyDTO.getId() != null) {
            throw new BadRequestAlertException("A new pharmacy cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pharmacyDTO = pharmacyService.save(pharmacyDTO);
        return ResponseEntity.created(new URI("/api/pharmacies/" + pharmacyDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pharmacyDTO.getId().toString()))
            .body(pharmacyDTO);
    }
    */

    /**
     * {@code PUT  /pharmacies/:id} : Updates an existing pharmacy.
     * MODIFIED: Users can only update their own pharmacy
     *
     * @param id the id of the pharmacyDTO to save.
     * @param pharmacyDTO the pharmacyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pharmacyDTO,
     * or with status {@code 400 (Bad Request)} if the pharmacyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pharmacyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PharmacyDTO> updatePharmacy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PharmacyDTO pharmacyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Pharmacy : {}, {}", id, pharmacyDTO);
        if (pharmacyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pharmacyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pharmacyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // Check that user can only update their own pharmacy
        checkPharmacyAccess(id);

        pharmacyDTO = pharmacyService.update(pharmacyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pharmacyDTO.getId().toString()))
            .body(pharmacyDTO);
    }

    /**
     * {@code PATCH  /pharmacies/:id} : Partial updates given fields of an existing pharmacy, field will ignore if it is null
     * MODIFIED: Users can only update their own pharmacy
     *
     * @param id the id of the pharmacyDTO to save.
     * @param pharmacyDTO the pharmacyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pharmacyDTO,
     * or with status {@code 400 (Bad Request)} if the pharmacyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pharmacyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pharmacyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PharmacyDTO> partialUpdatePharmacy(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PharmacyDTO pharmacyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Pharmacy partially : {}, {}", id, pharmacyDTO);
        if (pharmacyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pharmacyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pharmacyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        // Check that user can only update their own pharmacy
        checkPharmacyAccess(id);

        Optional<PharmacyDTO> result = pharmacyService.partialUpdate(pharmacyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pharmacyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pharmacies} : get the current user's pharmacy.
     * MODIFIED: Returns only the current user's pharmacy instead of all pharmacies
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list containing the user's pharmacy.
     */
    @GetMapping("")
    public ResponseEntity<List<PharmacyDTO>> getAllPharmacies(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get current user's pharmacy");
        User currentUser = getCurrentUser();

        if (currentUser.getPharmacy() == null) {
            throw new BadRequestAlertException("User has no pharmacy", ENTITY_NAME, "nopharmacy");
        }

        Optional<PharmacyDTO> pharmacyDTO = pharmacyService.findOne(currentUser.getPharmacy().getId());
        List<PharmacyDTO> pharmacies = pharmacyDTO.map(List::of).orElse(List.of());

        return ResponseEntity.ok().body(pharmacies);
    }

    /**
     * {@code GET  /pharmacies/:id} : get the "id" pharmacy.
     * MODIFIED: Users can only access their own pharmacy
     *
     * @param id the id of the pharmacyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pharmacyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PharmacyDTO> getPharmacy(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Pharmacy : {}", id);

        // Check that user can only access their own pharmacy
        checkPharmacyAccess(id);

        Optional<PharmacyDTO> pharmacyDTO = pharmacyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pharmacyDTO);
    }
    /**
     * {@code DELETE  /pharmacies/:id} : delete the "id" pharmacy.
     * DISABLED: Pharmacy deletion should not be accessible from client side
     *
     * @param id the id of the pharmacyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    /*
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacy(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Pharmacy : {}", id);
        pharmacyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
    */
}
