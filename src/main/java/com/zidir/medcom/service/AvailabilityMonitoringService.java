package com.zidir.medcom.service;

import com.zidir.medcom.config.ApplicationProperties;
import com.zidir.medcom.domain.Notification;
import com.zidir.medcom.domain.User;
import com.zidir.medcom.domain.WatchListItem;
import com.zidir.medcom.domain.enumeration.NotificationType;
import com.zidir.medcom.repository.NotificationRepository;
import com.zidir.medcom.repository.UserRepository;
import com.zidir.medcom.repository.WatchListItemRepository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for monitoring product availability and generating notifications.
 */
@Service
public class AvailabilityMonitoringService {

    private static final Logger LOG = LoggerFactory.getLogger(AvailabilityMonitoringService.class);

    private final WatchListItemRepository watchListItemRepository;

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    private final ProductAvailabilityService productAvailabilityService;

    private final ApplicationProperties applicationProperties;

    public AvailabilityMonitoringService(
        WatchListItemRepository watchListItemRepository,
        UserRepository userRepository,
        NotificationRepository notificationRepository,
        ProductAvailabilityService productAvailabilityService,
        ApplicationProperties applicationProperties
    ) {
        this.watchListItemRepository = watchListItemRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.productAvailabilityService = productAvailabilityService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Scheduled task to check product availability.
     * Uses dynamic scheduling based on application properties.
     * The cron expression is built to run every N minutes where N is configured in application.yml
     */
    @Scheduled(fixedDelayString = "#{${application.availability-check.interval-minutes:30} * 60000}")
    @Transactional
    public void checkAvailability() {
        LOG.info("Starting scheduled availability check");

        List<WatchListItem> watchListItems = watchListItemRepository.findAll();
        LOG.debug("Found {} watchlist items to check", watchListItems.size());

        int updatedCount = 0;
        int notificationCount = 0;

        for (WatchListItem item : watchListItems) {
            try {
                // Skip items without products
                if (item.getProduct() == null || item.getProduct().getCode() == null) {
                    LOG.warn("WatchListItem {} has no product or product code, skipping", item.getId());
                    continue;
                }

                String productCode = item.getProduct().getCode();
                LOG.debug("Checking availability for product code: {}", productCode);

                Map<String, Object> availabilityResult = productAvailabilityService.checkProductAvailability(productCode);
                Boolean currentAvailability = (Boolean) availabilityResult.get("available");

                if (currentAvailability == null) {
                    LOG.warn("Availability check returned null for product {}, skipping", productCode);
                    continue;
                }

                // Check if availability has changed
                if (hasAvailabilityChanged(item.getLastAvailability(), currentAvailability)) {
                    LOG.info(
                        "Availability changed for product {} (watchlist item {}): {} -> {}",
                        productCode,
                        item.getId(),
                        item.getLastAvailability(),
                        currentAvailability
                    );

                    // Update watchlist item
                    item.setLastAvailability(currentAvailability);
                    item.setLastAvailabilityTime(ZonedDateTime.now());
                    watchListItemRepository.save(item);
                    updatedCount++;

                    // Generate notifications
                    int notificationsCreated = generateNotifications(item, currentAvailability);
                    notificationCount += notificationsCreated;
                }
            } catch (Exception e) {
                LOG.error("Error checking availability for watchlist item {}: {}", item.getId(), e.getMessage(), e);
            }
        }

        LOG.info("Availability check completed. Updated {} items, created {} notifications", updatedCount, notificationCount);
    }

    /**
     * Check if availability has changed, considering null values.
     *
     * @param previousAvailability the previous availability status
     * @param currentAvailability the current availability status
     * @return true if availability has changed
     */
    private boolean hasAvailabilityChanged(Boolean previousAvailability, Boolean currentAvailability) {
        if (previousAvailability == null) {
            return true; // First time checking, always consider it a change
        }
        return !previousAvailability.equals(currentAvailability);
    }

    /**
     * Generate notifications for users based on watchlist item settings.
     *
     * @param watchListItem the watchlist item with changed availability
     * @param newAvailability the new availability status
     * @return the number of notifications created
     */
    private int generateNotifications(WatchListItem watchListItem, Boolean newAvailability) {
        List<User> usersToNotify;

        if (Boolean.TRUE.equals(watchListItem.getNotifyAllUsers())) {
            // Notify all users in the pharmacy
            if (watchListItem.getPharmacy() == null) {
                LOG.warn("WatchListItem {} has no pharmacy, cannot notify users", watchListItem.getId());
                return 0;
            }

            usersToNotify = userRepository.findByPharmacyId(watchListItem.getPharmacy().getId());
            LOG.debug("Notifying all {} users in pharmacy {}", usersToNotify.size(), watchListItem.getPharmacy().getId());
        } else {
            // Notify only the creator
            if (watchListItem.getCreatedBy() == null) {
                LOG.warn("WatchListItem {} has no creator and notifyAllUsers is false, cannot notify", watchListItem.getId());
                return 0;
            }

            usersToNotify = List.of(watchListItem.getCreatedBy());
            LOG.debug("Notifying creator user {} for watchlist item {}", watchListItem.getCreatedBy().getId(), watchListItem.getId());
        }

        int count = 0;
        for (User user : usersToNotify) {
            try {
                Notification notification = new Notification();
                notification.setUser(user);
                notification.setPharmacy(watchListItem.getPharmacy());
                notification.setWatchListItem(watchListItem);
                notification.setNotificationType(NotificationType.AVAILABILITY_CHANGE);

                String productName = watchListItem.getProduct().getName() != null
                    ? watchListItem.getProduct().getName()
                    : watchListItem.getProduct().getCode();

                String availabilityStatus = Boolean.TRUE.equals(newAvailability) ? "available" : "unavailable";
                notification.setMessage(String.format("Product '%s' is now %s", productName, availabilityStatus));

                notification.setSent(false);
                notification.setDelivered(false);
                notification.setFailed(false);

                notificationRepository.save(notification);
                count++;

                LOG.debug("Created notification {} for user {} about product {}", notification.getId(), user.getLogin(), productName);
            } catch (Exception e) {
                LOG.error("Error creating notification for user {}: {}", user.getId(), e.getMessage(), e);
            }
        }

        return count;
    }
}
