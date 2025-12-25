package com.zidir.medcom.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zidir.medcom.config.Constants;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;

/**
 * A DTO for creating a user, without the pharmacyId field which is automatically set by the system.
 * This DTO is used for user creation endpoints to prevent API consumers from setting the pharmacy ID.
 */
public class UserCreationDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @Pattern(regexp = Constants.LOGIN_REGEX)
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private Set<String> authorities;

    public UserCreationDTO() {
        // Empty constructor needed for Jackson.
    }

    // Copy constructor from AdminUserDTO to convert when needed
    public UserCreationDTO(AdminUserDTO adminUserDTO) {
        this.id = adminUserDTO.getId();
        this.login = adminUserDTO.getLogin();
        this.firstName = adminUserDTO.getFirstName();
        this.lastName = adminUserDTO.getLastName();
        this.email = adminUserDTO.getEmail();
        this.activated = adminUserDTO.isActivated();
        this.imageUrl = adminUserDTO.getImageUrl();
        this.langKey = adminUserDTO.getLangKey();
        this.authorities = adminUserDTO.getAuthorities();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserCreationDTO{" +
            "login='" + login + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", imageUrl='" + imageUrl + '\'' +
            ", activated=" + activated +
            ", langKey='" + langKey + '\'' +
            ", authorities=" + authorities +
            "}";
    }
}
