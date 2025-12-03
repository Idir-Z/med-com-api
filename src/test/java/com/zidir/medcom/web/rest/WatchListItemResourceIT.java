package com.zidir.medcom.web.rest;

import static com.zidir.medcom.domain.WatchListItemAsserts.*;
import static com.zidir.medcom.web.rest.TestUtil.createUpdateProxyForBean;
import static com.zidir.medcom.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidir.medcom.IntegrationTest;
import com.zidir.medcom.domain.WatchListItem;
import com.zidir.medcom.repository.WatchListItemRepository;
import com.zidir.medcom.service.dto.WatchListItemDTO;
import com.zidir.medcom.service.mapper.WatchListItemMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link WatchListItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WatchListItemResourceIT {

    private static final Boolean DEFAULT_LAST_AVAILABILITY = false;
    private static final Boolean UPDATED_LAST_AVAILABILITY = true;

    private static final ZonedDateTime DEFAULT_LAST_AVAILABILITY_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_AVAILABILITY_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/watch-list-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WatchListItemRepository watchListItemRepository;

    @Autowired
    private WatchListItemMapper watchListItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWatchListItemMockMvc;

    private WatchListItem watchListItem;

    private WatchListItem insertedWatchListItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchListItem createEntity() {
        return new WatchListItem().lastAvailability(DEFAULT_LAST_AVAILABILITY).lastAvailabilityTime(DEFAULT_LAST_AVAILABILITY_TIME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatchListItem createUpdatedEntity() {
        return new WatchListItem().lastAvailability(UPDATED_LAST_AVAILABILITY).lastAvailabilityTime(UPDATED_LAST_AVAILABILITY_TIME);
    }

    @BeforeEach
    void initTest() {
        watchListItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWatchListItem != null) {
            watchListItemRepository.delete(insertedWatchListItem);
            insertedWatchListItem = null;
        }
    }

    @Test
    @Transactional
    void createWatchListItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);
        var returnedWatchListItemDTO = om.readValue(
            restWatchListItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watchListItemDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WatchListItemDTO.class
        );

        // Validate the WatchListItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWatchListItem = watchListItemMapper.toEntity(returnedWatchListItemDTO);
        assertWatchListItemUpdatableFieldsEquals(returnedWatchListItem, getPersistedWatchListItem(returnedWatchListItem));

        insertedWatchListItem = returnedWatchListItem;
    }

    @Test
    @Transactional
    void createWatchListItemWithExistingId() throws Exception {
        // Create the WatchListItem with an existing ID
        watchListItem.setId(1L);
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatchListItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watchListItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllWatchListItems() throws Exception {
        // Initialize the database
        insertedWatchListItem = watchListItemRepository.saveAndFlush(watchListItem);

        // Get all the watchListItemList
        restWatchListItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watchListItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].lastAvailability").value(hasItem(DEFAULT_LAST_AVAILABILITY)))
            .andExpect(jsonPath("$.[*].lastAvailabilityTime").value(hasItem(sameInstant(DEFAULT_LAST_AVAILABILITY_TIME))));
    }

    @Test
    @Transactional
    void getWatchListItem() throws Exception {
        // Initialize the database
        insertedWatchListItem = watchListItemRepository.saveAndFlush(watchListItem);

        // Get the watchListItem
        restWatchListItemMockMvc
            .perform(get(ENTITY_API_URL_ID, watchListItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(watchListItem.getId().intValue()))
            .andExpect(jsonPath("$.lastAvailability").value(DEFAULT_LAST_AVAILABILITY))
            .andExpect(jsonPath("$.lastAvailabilityTime").value(sameInstant(DEFAULT_LAST_AVAILABILITY_TIME)));
    }

    @Test
    @Transactional
    void getNonExistingWatchListItem() throws Exception {
        // Get the watchListItem
        restWatchListItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWatchListItem() throws Exception {
        // Initialize the database
        insertedWatchListItem = watchListItemRepository.saveAndFlush(watchListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watchListItem
        WatchListItem updatedWatchListItem = watchListItemRepository.findById(watchListItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWatchListItem are not directly saved in db
        em.detach(updatedWatchListItem);
        updatedWatchListItem.lastAvailability(UPDATED_LAST_AVAILABILITY).lastAvailabilityTime(UPDATED_LAST_AVAILABILITY_TIME);
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(updatedWatchListItem);

        restWatchListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, watchListItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(watchListItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWatchListItemToMatchAllProperties(updatedWatchListItem);
    }

    @Test
    @Transactional
    void putNonExistingWatchListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchListItem.setId(longCount.incrementAndGet());

        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatchListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, watchListItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(watchListItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWatchListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchListItem.setId(longCount.incrementAndGet());

        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(watchListItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWatchListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchListItem.setId(longCount.incrementAndGet());

        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watchListItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWatchListItemWithPatch() throws Exception {
        // Initialize the database
        insertedWatchListItem = watchListItemRepository.saveAndFlush(watchListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watchListItem using partial update
        WatchListItem partialUpdatedWatchListItem = new WatchListItem();
        partialUpdatedWatchListItem.setId(watchListItem.getId());

        restWatchListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatchListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWatchListItem))
            )
            .andExpect(status().isOk());

        // Validate the WatchListItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWatchListItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWatchListItem, watchListItem),
            getPersistedWatchListItem(watchListItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateWatchListItemWithPatch() throws Exception {
        // Initialize the database
        insertedWatchListItem = watchListItemRepository.saveAndFlush(watchListItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watchListItem using partial update
        WatchListItem partialUpdatedWatchListItem = new WatchListItem();
        partialUpdatedWatchListItem.setId(watchListItem.getId());

        partialUpdatedWatchListItem.lastAvailability(UPDATED_LAST_AVAILABILITY).lastAvailabilityTime(UPDATED_LAST_AVAILABILITY_TIME);

        restWatchListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatchListItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWatchListItem))
            )
            .andExpect(status().isOk());

        // Validate the WatchListItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWatchListItemUpdatableFieldsEquals(partialUpdatedWatchListItem, getPersistedWatchListItem(partialUpdatedWatchListItem));
    }

    @Test
    @Transactional
    void patchNonExistingWatchListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchListItem.setId(longCount.incrementAndGet());

        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatchListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, watchListItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(watchListItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWatchListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchListItem.setId(longCount.incrementAndGet());

        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(watchListItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWatchListItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watchListItem.setId(longCount.incrementAndGet());

        // Create the WatchListItem
        WatchListItemDTO watchListItemDTO = watchListItemMapper.toDto(watchListItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatchListItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(watchListItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatchListItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWatchListItem() throws Exception {
        // Initialize the database
        insertedWatchListItem = watchListItemRepository.saveAndFlush(watchListItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the watchListItem
        restWatchListItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, watchListItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return watchListItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected WatchListItem getPersistedWatchListItem(WatchListItem watchListItem) {
        return watchListItemRepository.findById(watchListItem.getId()).orElseThrow();
    }

    protected void assertPersistedWatchListItemToMatchAllProperties(WatchListItem expectedWatchListItem) {
        assertWatchListItemAllPropertiesEquals(expectedWatchListItem, getPersistedWatchListItem(expectedWatchListItem));
    }

    protected void assertPersistedWatchListItemToMatchUpdatableProperties(WatchListItem expectedWatchListItem) {
        assertWatchListItemAllUpdatablePropertiesEquals(expectedWatchListItem, getPersistedWatchListItem(expectedWatchListItem));
    }
}
