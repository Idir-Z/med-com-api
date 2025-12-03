package com.zidir.medcom.service.dto;

import com.zidir.medcom.domain.enumeration.NotificationType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.zidir.medcom.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    private NotificationType notificationType;

    @Size(max = 2000)
    private String message;

    private ZonedDateTime readAt;

    private Boolean sent;

    private ZonedDateTime sentAt;

    private Boolean delivered;

    private ZonedDateTime deliveredAt;

    private Boolean failed;

    private ZonedDateTime failedAt;

    private String failureReason;

    private String externalMessageId;

    private PharmacyDTO pharmacy;

    private UserDTO user;

    private WatchListItemDTO watchListItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getReadAt() {
        return readAt;
    }

    public void setReadAt(ZonedDateTime readAt) {
        this.readAt = readAt;
    }

    public Boolean getSent() {
        return sent;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public ZonedDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public ZonedDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Boolean getFailed() {
        return failed;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public ZonedDateTime getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(ZonedDateTime failedAt) {
        this.failedAt = failedAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getExternalMessageId() {
        return externalMessageId;
    }

    public void setExternalMessageId(String externalMessageId) {
        this.externalMessageId = externalMessageId;
    }

    public PharmacyDTO getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(PharmacyDTO pharmacy) {
        this.pharmacy = pharmacy;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public WatchListItemDTO getWatchListItem() {
        return watchListItem;
    }

    public void setWatchListItem(WatchListItemDTO watchListItem) {
        this.watchListItem = watchListItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", notificationType='" + getNotificationType() + "'" +
            ", message='" + getMessage() + "'" +
            ", readAt='" + getReadAt() + "'" +
            ", sent='" + getSent() + "'" +
            ", sentAt='" + getSentAt() + "'" +
            ", delivered='" + getDelivered() + "'" +
            ", deliveredAt='" + getDeliveredAt() + "'" +
            ", failed='" + getFailed() + "'" +
            ", failedAt='" + getFailedAt() + "'" +
            ", failureReason='" + getFailureReason() + "'" +
            ", externalMessageId='" + getExternalMessageId() + "'" +
            ", pharmacy=" + getPharmacy() +
            ", user=" + getUser() +
            ", watchListItem=" + getWatchListItem() +
            "}";
    }
}
