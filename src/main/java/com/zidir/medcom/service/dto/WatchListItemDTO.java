package com.zidir.medcom.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.zidir.medcom.domain.WatchListItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class WatchListItemDTO implements Serializable {

    private Long id;

    private Boolean lastAvailability;

    private ZonedDateTime lastAvailabilityTime;

    private ProductDTO product;

    private PharmacyDTO pharmacy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getLastAvailability() {
        return lastAvailability;
    }

    public void setLastAvailability(Boolean lastAvailability) {
        this.lastAvailability = lastAvailability;
    }

    public ZonedDateTime getLastAvailabilityTime() {
        return lastAvailabilityTime;
    }

    public void setLastAvailabilityTime(ZonedDateTime lastAvailabilityTime) {
        this.lastAvailabilityTime = lastAvailabilityTime;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public PharmacyDTO getPharmacy() {
        return pharmacy;
    }

    public void setPharmacy(PharmacyDTO pharmacy) {
        this.pharmacy = pharmacy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WatchListItemDTO)) {
            return false;
        }

        WatchListItemDTO watchListItemDTO = (WatchListItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, watchListItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WatchListItemDTO{" +
            "id=" + getId() +
            ", lastAvailability='" + getLastAvailability() + "'" +
            ", lastAvailabilityTime='" + getLastAvailabilityTime() + "'" +
            ", product=" + getProduct() +
            ", pharmacy=" + getPharmacy() +
            "}";
    }
}
