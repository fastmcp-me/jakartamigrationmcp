package adrianmikula.jakartamigration.storage.service;

import adrianmikula.jakartamigration.config.FeatureFlagsProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for LocalLicenseStorageService (file-based storage).
 */
@DisplayName("LocalLicenseStorageService File-Based Storage Tests")
class LocalLicenseStorageServiceTest {

    @TempDir
    Path tempDir;

    private LocalLicenseStorageService service;
    private Path testStorageFile;

    @BeforeEach
    void setUp() {
        testStorageFile = tempDir.resolve("test_license_sessions.json");
        
        // Create service with test file path directly
        // The constructor takes the file path as a parameter
        service = new LocalLicenseStorageService(testStorageFile.toString());
    }

    @Test
    @DisplayName("Should store and retrieve license session by email")
    void shouldStoreAndRetrieveSessionByEmail() {
        // When
        service.storeSession("test@example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.PREMIUM, 24L);

        // Then
        FeatureFlagsProperties.LicenseTier tier = service.getTierByEmail("test@example.com");
        assertThat(tier).isEqualTo(FeatureFlagsProperties.LicenseTier.PREMIUM);
    }

    @Test
    @DisplayName("Should store and retrieve license session by license key")
    void shouldStoreAndRetrieveSessionByLicenseKey() {
        // When
        service.storeSession("test@example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.ENTERPRISE, 24L);

        // Then
        FeatureFlagsProperties.LicenseTier tier = service.getTierByLicenseKey("license_key_123");
        assertThat(tier).isEqualTo(FeatureFlagsProperties.LicenseTier.ENTERPRISE);
    }

    @Test
    @DisplayName("Should return null for non-existent email")
    void shouldReturnNullForNonExistentEmail() {
        // When
        FeatureFlagsProperties.LicenseTier tier = service.getTierByEmail("nonexistent@example.com");

        // Then
        assertThat(tier).isNull();
    }

    @Test
    @DisplayName("Should return null for expired session")
    void shouldReturnNullForExpiredSession() {
        // Given - store with very short TTL
        service.storeSession("test@example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.PREMIUM, 0L); // 0 hours = expired immediately

        // When
        FeatureFlagsProperties.LicenseTier tier = service.getTierByEmail("test@example.com");

        // Then
        assertThat(tier).isNull();
    }

    @Test
    @DisplayName("Should delete session by email")
    void shouldDeleteSessionByEmail() {
        // Given
        service.storeSession("test@example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.PREMIUM, 24L);

        // When
        service.deleteSession("test@example.com");

        // Then
        FeatureFlagsProperties.LicenseTier tier = service.getTierByEmail("test@example.com");
        assertThat(tier).isNull();
    }

    @Test
    @DisplayName("Should handle case-insensitive email")
    void shouldHandleCaseInsensitiveEmail() {
        // Given
        service.storeSession("Test@Example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.PREMIUM, 24L);

        // When/Then
        assertThat(service.getTierByEmail("test@example.com")).isEqualTo(FeatureFlagsProperties.LicenseTier.PREMIUM);
        assertThat(service.getTierByEmail("TEST@EXAMPLE.COM")).isEqualTo(FeatureFlagsProperties.LicenseTier.PREMIUM);
    }

    @Test
    @DisplayName("Should update existing session")
    void shouldUpdateExistingSession() {
        // Given
        service.storeSession("test@example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.PREMIUM, 24L);

        // When - update with new tier
        service.storeSession("test@example.com", "license_key_456", 
            FeatureFlagsProperties.LicenseTier.ENTERPRISE, 24L);

        // Then
        FeatureFlagsProperties.LicenseTier tier = service.getTierByEmail("test@example.com");
        assertThat(tier).isEqualTo(FeatureFlagsProperties.LicenseTier.ENTERPRISE);
        
        // License key should also be updated
        FeatureFlagsProperties.LicenseTier tierByKey = service.getTierByLicenseKey("license_key_456");
        assertThat(tierByKey).isEqualTo(FeatureFlagsProperties.LicenseTier.ENTERPRISE);
    }

    @Test
    @DisplayName("Should handle null email gracefully")
    void shouldHandleNullEmailGracefully() {
        // When/Then
        assertThat(service.getTierByEmail(null)).isNull();
        assertThat(service.getTierByEmail("")).isNull();
        assertThat(service.getTierByEmail("   ")).isNull();
    }

    @Test
    @DisplayName("Should use default TTL when not specified")
    void shouldUseDefaultTtlWhenNotSpecified() {
        // When
        service.storeSession("test@example.com", "license_key_123", 
            FeatureFlagsProperties.LicenseTier.PREMIUM, null);

        // Then - should still be valid (default is 24 hours)
        FeatureFlagsProperties.LicenseTier tier = service.getTierByEmail("test@example.com");
        assertThat(tier).isEqualTo(FeatureFlagsProperties.LicenseTier.PREMIUM);
    }
}

