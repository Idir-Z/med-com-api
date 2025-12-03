package com.zidir.medcom.web.rest;

import com.zidir.medcom.repository.WatchListItemRepository;
import com.zidir.medcom.service.WatchListItemService;
import com.zidir.medcom.service.dto.WatchListItemDTO;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.zidir.medcom.domain.WatchListItem}.
 */
@RestController
@RequestMapping("/api/watch-list-items")
public class WatchListItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListItemResource.class);

    private static final String ENTITY_NAME = "watchListItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WatchListItemService watchListItemService;

    private final WatchListItemRepository watchListItemRepository;

    public WatchListItemResource(WatchListItemService watchListItemService, WatchListItemRepository watchListItemRepository) {
        this.watchListItemService = watchListItemService;
        this.watchListItemRepository = watchListItemRepository;
    }

    /**
     * {@code POST  /watch-list-items} : Create a new watchListItem.
     *
     * @param watchListItemDTO the watchListItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new watchListItemDTO, or with status {@code 400 (Bad Request)} if the watchListItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WatchListItemDTO> createWatchListItem(@RequestBody WatchListItemDTO watchListItemDTO) throws URISyntaxException {
        LOG.debug("REST request to save WatchListItem : {}", watchListItemDTO);
        if (watchListItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new watchListItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        watchListItemDTO = watchListItemService.save(watchListItemDTO);
        return ResponseEntity.created(new URI("/api/watch-list-items/" + watchListItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, watchListItemDTO.getId().toString()))
            .body(watchListItemDTO);
    }

    /**
     * {@code PUT  /watch-list-items/:id} : Updates an existing watchListItem.
     *
     * @param id the id of the watchListItemDTO to save.
     * @param watchListItemDTO the watchListItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchListItemDTO,
     * or with status {@code 400 (Bad Request)} if the watchListItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the watchListItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WatchListItemDTO> updateWatchListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WatchListItemDTO watchListItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WatchListItem : {}, {}", id, watchListItemDTO);
        if (watchListItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchListItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watchListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        watchListItemDTO = watchListItemService.update(watchListItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, watchListItemDTO.getId().toString()))
            .body(watchListItemDTO);
    }

    /**
     * {@code PATCH  /watch-list-items/:id} : Partial updates given fields of an existing watchListItem, field will ignore if it is null
     *
     * @param id the id of the watchListItemDTO to save.
     * @param watchListItemDTO the watchListItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated watchListItemDTO,
     * or with status {@code 400 (Bad Request)} if the watchListItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the watchListItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the watchListItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WatchListItemDTO> partialUpdateWatchListItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WatchListItemDTO watchListItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WatchListItem partially : {}, {}", id, watchListItemDTO);
        if (watchListItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, watchListItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!watchListItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WatchListItemDTO> result = watchListItemService.partialUpdate(watchListItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, watchListItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /watch-list-items} : get all the watchListItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of watchListItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WatchListItemDTO>> getAllWatchListItems(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of WatchListItems");
        Page<WatchListItemDTO> page = watchListItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /watch-list-items/:id} : get the "id" watchListItem.
     *
     * @param id the id of the watchListItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the watchListItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WatchListItemDTO> getWatchListItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WatchListItem : {}", id);
        Optional<WatchListItemDTO> watchListItemDTO = watchListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(watchListItemDTO);
    }

    /**
     * {@code DELETE  /watch-list-items/:id} : delete the "id" watchListItem.
     *
     * @param id the id of the watchListItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWatchListItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WatchListItem : {}", id);
        watchListItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
