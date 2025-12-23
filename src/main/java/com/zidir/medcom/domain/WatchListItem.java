package com.zidir.medcom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WatchListItem.
 */
@Entity
@Table(name = "watch_list_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WatchListItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "last_availability")
    private Boolean lastAvailability;

    @Column(name = "last_availability_time")
    private ZonedDateTime lastAvailabilityTime;

    @Column(name = "notify_all_users", nullable = false)
    private Boolean notifyAllUsers = true;

    @ManyToOne(fetch = FetchType.LAZY)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "watchListItems" }, allowSetters = true)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Pharmacy pharmacy;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "watchListItem")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pharmacy", "user", "watchListItem" }, allowSetters = true)
    private Set<Notification> notifications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WatchListItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLastAvailability() {
        return this.lastAvailability;
    }

    public WatchListItem lastAvailability(Boolean lastAvailability) {
        this.setLastAvailability(lastAvailability);
        return this;
    }

    public void setLastAvailability(Boolean lastAvailability) {
        this.lastAvailability = lastAvailability;
    }

    public ZonedDateTime getLastAvailabilityTime() {
        return this.lastAvailabilityTime;
    }

    public WatchListItem lastAvailabilityTime(ZonedDateTime lastAvailabilityTime) {
        this.setLastAvailabilityTime(lastAvailabilityTime);
        return this;
    }

    public void setLastAvailabilityTime(ZonedDateTime lastAvailabilityTime) {
        this.lastAvailabilityTime = lastAvailabilityTime;
    }

    public Boolean getNotifyAllUsers() {
        return this.notifyAllUsers;
    }

    public WatchListItem notifyAllUsers(Boolean notifyAllUsers) {
        this.setNotifyAllUsers(notifyAllUsers);
        return this;
    }

    public void setNotifyAllUsers(Boolean notifyAllUsers) {
        this.notifyAllUsers = notifyAllUsers;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public WatchListItem createdBy(User user) {
        this.setCreatedBy(user);
        return this;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public WatchListItem product(Product product) {
        this.setProduct(product);
        return this;
    }

    public Pharmacy getPharmacy() {
        return this.pharmacy;
    }

    public void setPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }

    public WatchListItem pharmacy(Pharmacy pharmacy) {
        this.setPharmacy(pharmacy);
        return this;
    }

    public Set<Notification> getNotifications() {
        return this.notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        if (this.notifications != null) {
            this.notifications.forEach(i -> i.setWatchListItem(null));
        }
        if (notifications != null) {
            notifications.forEach(i -> i.setWatchListItem(this));
        }
        this.notifications = notifications;
    }

    public WatchListItem notifications(Set<Notification> notifications) {
        this.setNotifications(notifications);
        return this;
    }

    public WatchListItem addNotification(Notification notification) {
        this.notifications.add(notification);
        notification.setWatchListItem(this);
        return this;
    }

    public WatchListItem removeNotification(Notification notification) {
        this.notifications.remove(notification);
        notification.setWatchListItem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WatchListItem)) {
            return false;
        }
        return getId() != null && getId().equals(((WatchListItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WatchListItem{" +
            "id=" + getId() +
            ", lastAvailability='" + getLastAvailability() + "'" +
            ", lastAvailabilityTime='" + getLastAvailabilityTime() + "'" +
            ", notifyAllUsers='" + getNotifyAllUsers() + "'" +
            "}";
    }
}
