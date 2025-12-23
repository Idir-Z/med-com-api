package com.zidir.medcom.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Medcom.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();

    private final AvailabilityCheck availabilityCheck = new AvailabilityCheck();

    private final SupplierApi supplierApi = new SupplierApi();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public AvailabilityCheck getAvailabilityCheck() {
        return availabilityCheck;
    }

    public SupplierApi getSupplierApi() {
        return supplierApi;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class AvailabilityCheck {

        private Integer intervalMinutes = 30;

        public Integer getIntervalMinutes() {
            return intervalMinutes;
        }

        public void setIntervalMinutes(Integer intervalMinutes) {
            this.intervalMinutes = intervalMinutes;
        }
    }

    public static class SupplierApi {

        private Boolean enabled = false;

        private String baseUrl = "";

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }
    // jhipster-needle-application-properties-property-class
}
