package com.zidir.medcom.web.rest.vm;

import jakarta.validation.constraints.*;

/**
 * View Model for pharmacy registration with admin user.
 */
public class PharmacyRegistrationVM {

    // Pharmacy fields
    @NotBlank
    @Size(max = 255)
    private String pharmacyName;

    @Size(max = 255)
    private String address;

    @Email
    @Size(min = 5, max = 254)
    private String pharmacyEmail;

    @Size(max = 20)
    private String phone;

    @Size(max = 255)
    private String website;

    // Admin user fields
    @NotBlank
    @Pattern(regexp = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$")
    @Size(min = 1, max = 50)
    private String adminLogin;

    @NotBlank
    @Size(min = 4, max = 100)
    private String adminPassword;

    @Size(max = 50)
    private String adminFirstName;

    @Size(max = 50)
    private String adminLastName;

    @Email
    @Size(min = 5, max = 254)
    @NotBlank
    private String adminEmail;

    @Size(min = 2, max = 10)
    private String langKey;

    // Getters and Setters
    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPharmacyEmail() {
        return pharmacyEmail;
    }

    public void setPharmacyEmail(String pharmacyEmail) {
        this.pharmacyEmail = pharmacyEmail;
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

    public String getAdminLogin() {
        return adminLogin;
    }

    public void setAdminLogin(String adminLogin) {
        this.adminLogin = adminLogin;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminFirstName() {
        return adminFirstName;
    }

    public void setAdminFirstName(String adminFirstName) {
        this.adminFirstName = adminFirstName;
    }

    public String getAdminLastName() {
        return adminLastName;
    }

    public void setAdminLastName(String adminLastName) {
        this.adminLastName = adminLastName;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    @Override
    public String toString() {
        return (
            "PharmacyRegistrationVM{" +
            "pharmacyName='" +
            pharmacyName +
            '\'' +
            ", address='" +
            address +
            '\'' +
            ", pharmacyEmail='" +
            pharmacyEmail +
            '\'' +
            ", phone='" +
            phone +
            '\'' +
            ", adminLogin='" +
            adminLogin +
            '\'' +
            ", adminEmail='" +
            adminEmail +
            '\'' +
            '}'
        );
    }
}
