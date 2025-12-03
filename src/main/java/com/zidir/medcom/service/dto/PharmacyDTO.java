package com.zidir.medcom.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.zidir.medcom.domain.Pharmacy} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PharmacyDTO implements Serializable {

    private Long id;

    private String name;

    private String address;

    private String email;

    private String phone;

    private String website;

    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PharmacyDTO)) {
            return false;
        }

        PharmacyDTO pharmacyDTO = (PharmacyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, pharmacyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PharmacyDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", email='" + getEmail() + "'" +
            ", phone='" + getPhone() + "'" +
            ", website='" + getWebsite() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
