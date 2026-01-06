package com.bugbounty.jakartamigration.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service for license key validation and tier determination.
 * 
 * This service delegates to ApifyLicenseService for Apify-based validation,
 * with fallback to simple local validation.
 * 
 * Future implementations could integrate with:
 * - Stripe for subscription validation (planned)
 * - Custom license server
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseService {

    private final ApifyLicenseService apifyLicenseService;

    /**
     * Validate a license key and return the associated tier.
     * 
     * This method:
     * 1. First tries Apify validation (if enabled)
     * 2. Falls back to simple pattern matching
     * 
     * @param licenseKey The license key to validate
     * @return The license tier if valid, null if invalid
     */
    public FeatureFlagsProperties.LicenseTier validateLicense(String licenseKey) {
        if (licenseKey == null || licenseKey.isBlank()) {
            return null;
        }

        // Try Apify validation first
        FeatureFlagsProperties.LicenseTier tier = apifyLicenseService.validateLicense(licenseKey);
        if (tier != null) {
            return tier;
        }

        // Fallback to simple validation for test keys
        // Keys starting with "PREMIUM-" are premium tier
        // Keys starting with "ENTERPRISE-" are enterprise tier
        if (licenseKey.startsWith("PREMIUM-")) {
            log.debug("Valid premium license key detected (test key)");
            return FeatureFlagsProperties.LicenseTier.PREMIUM;
        }

        if (licenseKey.startsWith("ENTERPRISE-")) {
            log.debug("Valid enterprise license key detected (test key)");
            return FeatureFlagsProperties.LicenseTier.ENTERPRISE;
        }

        log.debug("Invalid license key format: {}", maskKey(licenseKey));
        return null;
    }

    /**
     * Mask license key for logging.
     */
    private String maskKey(String key) {
        if (key == null || key.length() <= 8) {
            return "***";
        }
        return key.substring(0, 4) + "..." + key.substring(key.length() - 4);
    }

    /**
     * Check if a license key is valid (not expired, properly signed, etc.).
     * 
     * @param licenseKey The license key to check
     * @return true if the license is valid, false otherwise
     */
    public boolean isValidLicense(String licenseKey) {
        return validateLicense(licenseKey) != null;
    }

    /**
     * Get license expiration date (future implementation).
     * 
     * @param licenseKey The license key
     * @return Expiration date, or null if perpetual or invalid
     */
    public java.time.LocalDate getExpirationDate(String licenseKey) {
        // TODO: Extract expiration date from license key
        return null;
    }

    /**
     * Check if a license is expired.
     * 
     * @param licenseKey The license key
     * @return true if expired, false otherwise
     */
    public boolean isExpired(String licenseKey) {
        java.time.LocalDate expiration = getExpirationDate(licenseKey);
        if (expiration == null) {
            return false; // Perpetual license or no expiration
        }
        return expiration.isBefore(java.time.LocalDate.now());
    }
}

