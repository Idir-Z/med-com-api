package com.zidir.medcom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zidir.medcom.domain.enumeration.NotificationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationType notificationType;

    @Size(max = 2000)
    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "read_at")
    private ZonedDateTime readAt;

    @Column(name = "sent")
    private Boolean sent;

    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    @Column(name = "delivered")
    private Boolean delivered;

    @Column(name = "delivered_at")
    private ZonedDateTime deliveredAt;

    @Column(name = "failed")
    private Boolean failed;

    @Column(name = "failed_at")
    private ZonedDateTime failedAt;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "external_message_id")
    private String externalMessageId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pharmacy pharmacy;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "product", "pharmacy", "notifications" }, allowSetters = true)
    private WatchListItem watchListItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getNotificationType() {
        return this.notificationType;
    }

    public Notification notificationType(NotificationType notificationType) {
        this.setNotificationType(notificationType);
        return this;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getReadAt() {
        return this.readAt;
    }

    public Notification readAt(ZonedDateTime readAt) {
        this.setReadAt(readAt);
        return this;
    }

    public void setReadAt(ZonedDateTime readAt) {
        this.readAt = readAt;
    }

    public Boolean getSent() {
        return this.sent;
    }

    public Notification sent(Boolean sent) {
        this.setSent(sent);
        return this;
    }

    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    public ZonedDateTime getSentAt() {
        return this.sentAt;
    }

    public Notification sentAt(ZonedDateTime sentAt) {
        this.setSentAt(sentAt);
        return this;
    }

    public void setSentAt(ZonedDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Boolean getDelivered() {
        return this.delivered;
    }

    public Notification delivered(Boolean delivered) {
        this.setDelivered(delivered);
        return this;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public ZonedDateTime getDeliveredAt() {
        return this.deliveredAt;
    }

    public Notification deliveredAt(ZonedDateTime deliveredAt) {
        this.setDeliveredAt(deliveredAt);
        return this;
    }

    public void setDeliveredAt(ZonedDateTime deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    public Boolean getFailed() {
        return this.failed;
    }

    public Notification failed(Boolean failed) {
        this.setFailed(failed);
        return this;
    }

    public void setFailed(Boolean failed) {
        this.failed = failed;
    }

    public ZonedDateTime getFailedAt() {
        return this.failedAt;
    }

    public Notification failedAt(ZonedDateTime failedAt) {
        this.setFailedAt(failedAt);
        return this;
    }

    public void setFailedAt(ZonedDateTime failedAt) {
        this.failedAt = failedAt;
    }

    public String getFailureReason() {
        return this.failureReason;
    }

    public Notification failureReason(String failureReason) {
        this.setFailureReason(failureReason);
        return this;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getExternalMessageId() {
        return this.externalMessageId;
    }

    public Notification externalMessageId(String externalMessageId) {
        this.setExternalMessageId(externalMessageId);
        return this;
    }

    public void setExternalMessageId(String externalMessageId) {
        this.externalMessageId = externalMessageId;
    }

    public Pharmacy getPharmacy() {
        return this.pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public Notification pharmacy(Pharmacy pharmacy) {
        this.setPharmacy(pharmacy);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Notification user(User user) {
        this.setUser(user);
        return this;
    }

    public WatchListItem getWatchListItem() {
        return this.watchListItem;
    }

    public void setWatchListItem(WatchListItem watchListItem) {
        this.watchListItem = watchListItem;
    }

    public Notification watchListItem(WatchListItem watchListItem) {
        this.setWatchListItem(watchListItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
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
            "}";
    }
}
