package com.zidir.medcom.service;

import com.zidir.medcom.domain.Notification;
import com.zidir.medcom.domain.User;
import com.zidir.medcom.repository.NotificationRepository;
import com.zidir.medcom.repository.UserRepository;
import com.zidir.medcom.security.SecurityUtils;
import com.zidir.medcom.service.dto.NotificationDTO;
import com.zidir.medcom.service.mapper.NotificationMapper;
import com.zidir.medcom.web.rest.errors.BadRequestAlertException;
import java.time.ZonedDateTime;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.zidir.medcom.domain.Notification}.
 */
@Service
@Transactional
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    private final UserRepository userRepository;

    public NotificationService(
        NotificationRepository notificationRepository,
        NotificationMapper notificationMapper,
        UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
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
            .orElseThrow(() -> new BadRequestAlertException("User not authenticated", "notification", "notauthenticated"));

        return userRepository
            .findOneByLogin(login)
            .orElseThrow(() -> new BadRequestAlertException("User not found", "notification", "usernotfound"));
    }

    /**
     * Save a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO save(NotificationDTO notificationDTO) {
        LOG.debug("Request to save Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Update a notification.
     *
     * @param notificationDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificationDTO update(NotificationDTO notificationDTO) {
        LOG.debug("Request to update Notification : {}", notificationDTO);
        Notification notification = notificationMapper.toEntity(notificationDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toDto(notification);
    }

    /**
     * Partially update a notification.
     *
     * @param notificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificationDTO> partialUpdate(NotificationDTO notificationDTO) {
        LOG.debug("Request to partially update Notification : {}", notificationDTO);

        return notificationRepository
            .findById(notificationDTO.getId())
            .map(existingNotification -> {
                notificationMapper.partialUpdate(existingNotification, notificationDTO);

                return existingNotification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Notifications");
        return notificationRepository.findAll(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get all the notifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NotificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return notificationRepository.findAllWithEagerRelationships(pageable).map(notificationMapper::toDto);
    }

    /**
     * Get one notification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificationDTO> findOne(Long id) {
        LOG.debug("Request to get Notification : {}", id);
        return notificationRepository.findOneWithEagerRelationships(id).map(notificationMapper::toDto);
    }

    /**
     * Delete the notification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Notification : {}", id);
        notificationRepository.deleteById(id);
    }

    /**
     * Get notifications for the current user.
     *
     * @param pageable the pagination information.
     * @return the list of notifications for the current user.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findByCurrentUser(Pageable pageable) {
        LOG.debug("Request to get notifications for current user");
        User currentUser = getCurrentUser();
        return notificationRepository.findByUserId(currentUser.getId(), pageable).map(notificationMapper::toDto);
    }

    /**
     * Get notifications for the current user's pharmacy.
     *
     * @param pageable the pagination information.
     * @return the list of notifications for the current user's pharmacy.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findByCurrentUserPharmacy(Pageable pageable) {
        LOG.debug("Request to get notifications for current user's pharmacy");
        User currentUser = getCurrentUser();

        if (currentUser.getPharmacy() == null) {
            throw new BadRequestAlertException("User has no pharmacy", "notification", "nopharmacy");
        }

        return notificationRepository.findByPharmacyId(currentUser.getPharmacy().getId(), pageable).map(notificationMapper::toDto);
    }

    /**
     * Get unread notifications for the current user.
     *
     * @param pageable the pagination information.
     * @return the list of unread notifications for the current user.
     */
    @Transactional(readOnly = true)
    public Page<NotificationDTO> findUnreadByCurrentUser(Pageable pageable) {
        LOG.debug("Request to get unread notifications for current user");
        User currentUser = getCurrentUser();
        return notificationRepository.findByUserIdAndReadAtIsNull(currentUser.getId(), pageable).map(notificationMapper::toDto);
    }

    /**
     * Mark a notification as read.
     *
     * @param id the id of the notification.
     * @return the updated notification.
     */
    public Optional<NotificationDTO> markAsRead(Long id) {
        LOG.debug("Request to mark notification {} as read", id);

        return notificationRepository
            .findById(id)
            .map(notification -> {
                User currentUser = getCurrentUser();

                // Verify that the notification belongs to the current user
                if (notification.getUser() == null || !notification.getUser().getId().equals(currentUser.getId())) {
                    throw new BadRequestAlertException("Cannot mark another user's notification as read", "notification", "accessdenied");
                }

                if (notification.getReadAt() == null) {
                    notification.setReadAt(ZonedDateTime.now());
                }

                return notification;
            })
            .map(notificationRepository::save)
            .map(notificationMapper::toDto);
    }

    /**
     * Mark all notifications as read for the current user.
     *
     * @return the number of notifications marked as read.
     */
    public long markAllAsReadForCurrentUser() {
        LOG.debug("Request to mark all notifications as read for current user");

        User currentUser = getCurrentUser();
        ZonedDateTime now = ZonedDateTime.now();

        return notificationRepository.markAllAsReadForUser(currentUser.getId(), now);
    }

    /**
     * Count unread notifications for the current user.
     *
     * @return the count of unread notifications.
     */
    @Transactional(readOnly = true)
    public long countUnreadByCurrentUser() {
        LOG.debug("Request to count unread notifications for current user");
        User currentUser = getCurrentUser();
        return notificationRepository.countByUserIdAndReadAtIsNull(currentUser.getId());
    }
}
