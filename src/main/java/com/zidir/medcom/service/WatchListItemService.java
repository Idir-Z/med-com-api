package com.zidir.medcom.service;

import com.zidir.medcom.domain.User;
import com.zidir.medcom.domain.WatchListItem;
import com.zidir.medcom.repository.UserRepository;
import com.zidir.medcom.repository.WatchListItemRepository;
import com.zidir.medcom.security.SecurityUtils;
import com.zidir.medcom.service.dto.WatchListItemDTO;
import com.zidir.medcom.service.mapper.WatchListItemMapper;
import com.zidir.medcom.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.zidir.medcom.domain.WatchListItem}.
 */
@Service
@Transactional
public class WatchListItemService {

    private static final Logger LOG = LoggerFactory.getLogger(WatchListItemService.class);

    private final WatchListItemRepository watchListItemRepository;

    private final WatchListItemMapper watchListItemMapper;

    private final UserRepository userRepository;

    public WatchListItemService(
        WatchListItemRepository watchListItemRepository,
        WatchListItemMapper watchListItemMapper,
        UserRepository userRepository
    ) {
        this.watchListItemRepository = watchListItemRepository;
        this.watchListItemMapper = watchListItemMapper;
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
            .orElseThrow(() -> new BadRequestAlertException("User not authenticated", "watchListItem", "notauthenticated"));

        return userRepository
            .findOneByLogin(login)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "watchListItem", "usernotfound"));
    }

    /**
     * Check if the current user can access a watchlist item.
     *
     * @param watchListItem the watchlist item to check
     * @throws AccessDeniedException if user cannot access the item
     */
    private void checkAccess(WatchListItem watchListItem) {
        User currentUser = getCurrentUser();

        if (currentUser.getPharmacy() == null) {
            throw new BadRequestAlertException("User has no pharmacy", "watchListItem", "nopharmacy");
        }

        if (watchListItem.getPharmacy() == null || !currentUser.getPharmacy().getId().equals(watchListItem.getPharmacy().getId())) {
            throw new AccessDeniedException("User can only access watchlist items from their own pharmacy");
        }
    }

    /**
     * Save a watchListItem.
     * Automatically sets the createdBy to the current user and pharmacy to the user's pharmacy.
     *
     * @param watchListItemDTO the entity to save.
     * @return the persisted entity.
     */
    public WatchListItemDTO save(WatchListItemDTO watchListItemDTO) {
        LOG.debug("Request to save WatchListItem : {}", watchListItemDTO);

        User currentUser = getCurrentUser();

        if (currentUser.getPharmacy() == null) {
            throw new BadRequestAlertException("User has no pharmacy", "watchListItem", "nopharmacy");
        }

        WatchListItem watchListItem = watchListItemMapper.toEntity(watchListItemDTO);

        // Set the createdBy to current user
        watchListItem.setCreatedBy(currentUser);

        // Set the pharmacy to current user's pharmacy
        watchListItem.setPharmacy(currentUser.getPharmacy());

        // Set default value for notifyAllUsers if not specified
        if (watchListItem.getNotifyAllUsers() == null) {
            watchListItem.setNotifyAllUsers(true);
        }

        watchListItem = watchListItemRepository.save(watchListItem);
        return watchListItemMapper.toDto(watchListItem);
    }

    /**
     * Update a watchListItem.
     * Checks that the current user has access to the watchlist item's pharmacy.
     *
     * @param watchListItemDTO the entity to save.
     * @return the persisted entity.
     */
    public WatchListItemDTO update(WatchListItemDTO watchListItemDTO) {
        LOG.debug("Request to update WatchListItem : {}", watchListItemDTO);

        // Check access before updating
        WatchListItem existingItem = watchListItemRepository
            .findById(watchListItemDTO.getId())
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "watchListItem", "idnotfound"));

        checkAccess(existingItem);

        WatchListItem watchListItem = watchListItemMapper.toEntity(watchListItemDTO);
        watchListItem = watchListItemRepository.save(watchListItem);
        return watchListItemMapper.toDto(watchListItem);
    }

    /**
     * Partially update a watchListItem.
     * Checks that the current user has access to the watchlist item's pharmacy.
     *
     * @param watchListItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WatchListItemDTO> partialUpdate(WatchListItemDTO watchListItemDTO) {
        LOG.debug("Request to partially update WatchListItem : {}", watchListItemDTO);

        return watchListItemRepository
            .findById(watchListItemDTO.getId())
            .map(existingWatchListItem -> {
                // Check access before updating
                checkAccess(existingWatchListItem);

                watchListItemMapper.partialUpdate(existingWatchListItem, watchListItemDTO);

                return existingWatchListItem;
            })
            .map(watchListItemRepository::save)
            .map(watchListItemMapper::toDto);
    }

    /**
     * Get all the watchListItems for the current user's pharmacy.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WatchListItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WatchListItems");

        User currentUser = getCurrentUser();

        if (currentUser.getPharmacy() == null) {
            throw new BadRequestAlertException("User has no pharmacy", "watchListItem", "nopharmacy");
        }

        return watchListItemRepository.findByPharmacyId(currentUser.getPharmacy().getId(), pageable).map(watchListItemMapper::toDto);
    }

    /**
     * Get one watchListItem by id.
     * Checks that the current user has access to the watchlist item's pharmacy.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WatchListItemDTO> findOne(Long id) {
        LOG.debug("Request to get WatchListItem : {}", id);

        return watchListItemRepository
            .findById(id)
            .map(watchListItem -> {
                checkAccess(watchListItem);
                return watchListItem;
            })
            .map(watchListItemMapper::toDto);
    }

    /**
     * Delete the watchListItem by id.
     * Checks that the current user has access to the watchlist item's pharmacy.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WatchListItem : {}", id);

        WatchListItem watchListItem = watchListItemRepository
            .findById(id)
            .orElseThrow(() -> new BadRequestAlertException("Entity not found", "watchListItem", "idnotfound"));

        checkAccess(watchListItem);

        watchListItemRepository.deleteById(id);
    }
}
