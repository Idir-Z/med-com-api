package com.zidir.medcom.service;

import java.util.Map;

/**
 * Service interface for checking product availability via external supplier API.
 */
public interface ProductAvailabilityService {
    /**
     * Check product availability by product code.
     *
     * @param productCode the product code to check
     * @return Map containing availability information with keys:
     *         - "available" (Boolean): true if product is available
     *         - "error" (String, optional): error message if check failed
     *         - "placeholder" (Boolean, optional): true if using placeholder data
     */
    Map<String, Object> checkProductAvailability(String productCode);
}
