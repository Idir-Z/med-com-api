package com.zidir.medcom.service.impl;

import com.zidir.medcom.config.ApplicationProperties;
import com.zidir.medcom.service.ProductAvailabilityService;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service implementation for checking product availability via external supplier API.
 */
@Service
public class ProductAvailabilityServiceImpl implements ProductAvailabilityService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductAvailabilityServiceImpl.class);

    private final RestTemplate restTemplate;

    private final ApplicationProperties applicationProperties;

    public ProductAvailabilityServiceImpl(RestTemplate restTemplate, ApplicationProperties applicationProperties) {
        this.restTemplate = restTemplate;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public Map<String, Object> checkProductAvailability(String productCode) {
        Map<String, Object> result = new HashMap<>();

        if (!applicationProperties.getSupplierApi().getEnabled()) {
            LOG.debug("Supplier API is disabled, returning placeholder data for product: {}", productCode);
            // Placeholder: simulate availability check
            result.put("available", Math.random() > 0.5); // Random availability for testing
            result.put("placeholder", true);
            return result;
        }

        try {
            String url = applicationProperties.getSupplierApi().getBaseUrl() + "/products/" + productCode + "/availability";
            LOG.debug("Checking availability for product {} at URL: {}", productCode, url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                result = response.getBody();
                LOG.debug("Successfully retrieved availability for product {}: {}", productCode, result.get("available"));
            } else {
                LOG.warn("Unexpected response from supplier API for product {}: {}", productCode, response.getStatusCode());
                result.put("available", false);
                result.put("error", "Unexpected response from supplier API");
            }
        } catch (RestClientException e) {
            LOG.error("Error checking availability for product {}: {}", productCode, e.getMessage());
            result.put("available", false);
            result.put("error", e.getMessage());
        }

        return result;
    }
}
