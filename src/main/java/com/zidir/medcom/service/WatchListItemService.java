package com.zidir.medcom.service;

import com.zidir.medcom.domain.WatchListItem;
import com.zidir.medcom.repository.WatchListItemRepository;
import com.zidir.medcom.service.dto.WatchListItemDTO;
import com.zidir.medcom.service.mapper.WatchListItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public WatchListItemService(WatchListItemRepository watchListItemRepository, WatchListItemMapper watchListItemMapper) {
        this.watchListItemRepository = watchListItemRepository;
        this.watchListItemMapper = watchListItemMapper;
    }

    /**
     * Save a watchListItem.
     *
     * @param watchListItemDTO the entity to save.
     * @return the persisted entity.
     */
    public WatchListItemDTO save(WatchListItemDTO watchListItemDTO) {
        LOG.debug("Request to save WatchListItem : {}", watchListItemDTO);
        WatchListItem watchListItem = watchListItemMapper.toEntity(watchListItemDTO);
        watchListItem = watchListItemRepository.save(watchListItem);
        return watchListItemMapper.toDto(watchListItem);
    }

    /**
     * Update a watchListItem.
     *
     * @param watchListItemDTO the entity to save.
     * @return the persisted entity.
     */
    public WatchListItemDTO update(WatchListItemDTO watchListItemDTO) {
        LOG.debug("Request to update WatchListItem : {}", watchListItemDTO);
        WatchListItem watchListItem = watchListItemMapper.toEntity(watchListItemDTO);
        watchListItem = watchListItemRepository.save(watchListItem);
        return watchListItemMapper.toDto(watchListItem);
    }

    /**
     * Partially update a watchListItem.
     *
     * @param watchListItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<WatchListItemDTO> partialUpdate(WatchListItemDTO watchListItemDTO) {
        LOG.debug("Request to partially update WatchListItem : {}", watchListItemDTO);

        return watchListItemRepository
            .findById(watchListItemDTO.getId())
            .map(existingWatchListItem -> {
                watchListItemMapper.partialUpdate(existingWatchListItem, watchListItemDTO);

                return existingWatchListItem;
            })
            .map(watchListItemRepository::save)
            .map(watchListItemMapper::toDto);
    }

    /**
     * Get all the watchListItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<WatchListItemDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all WatchListItems");
        return watchListItemRepository.findAll(pageable).map(watchListItemMapper::toDto);
    }

    /**
     * Get one watchListItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<WatchListItemDTO> findOne(Long id) {
        LOG.debug("Request to get WatchListItem : {}", id);
        return watchListItemRepository.findById(id).map(watchListItemMapper::toDto);
    }

    /**
     * Delete the watchListItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete WatchListItem : {}", id);
        watchListItemRepository.deleteById(id);
    }
}
