package com.zidir.medcom.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "official_url")
    private String officialUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "product", "pharmacy", "notifications" }, allowSetters = true)
    private Set<WatchListItem> watchListItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public Product code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOfficialUrl() {
        return this.officialUrl;
    }

    public Product officialUrl(String officialUrl) {
        this.setOfficialUrl(officialUrl);
        return this;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }

    public Set<WatchListItem> getWatchListItems() {
        return this.watchListItems;
    }

    public void setWatchListItems(Set<WatchListItem> watchListItems) {
        if (this.watchListItems != null) {
            this.watchListItems.forEach(i -> i.setProduct(null));
        }
        if (watchListItems != null) {
            watchListItems.forEach(i -> i.setProduct(this));
        }
        this.watchListItems = watchListItems;
    }

    public Product watchListItems(Set<WatchListItem> watchListItems) {
        this.setWatchListItems(watchListItems);
        return this;
    }

    public Product addWatchListItem(WatchListItem watchListItem) {
        this.watchListItems.add(watchListItem);
        watchListItem.setProduct(this);
        return this;
    }

    public Product removeWatchListItem(WatchListItem watchListItem) {
        this.watchListItems.remove(watchListItem);
        watchListItem.setProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", code='" + getCode() + "'" +
            ", officialUrl='" + getOfficialUrl() + "'" +
            "}";
    }
}
