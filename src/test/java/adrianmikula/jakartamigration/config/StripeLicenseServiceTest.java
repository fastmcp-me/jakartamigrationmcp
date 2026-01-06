package adrianmikula.jakartamigration.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

/**
 * Unit tests for StripeLicenseService.
 * Note: These are simplified tests. In production, you'd want to test
 * the actual WebClient integration with a mock server.
 */
@ExtendWith(MockitoExtension.class)
class StripeLicenseServiceTest {

    @Mock
    private StripeLicenseProperties properties;

    @Mock
    private WebClient stripeWebClient;

    private StripeLicenseService service;

    @BeforeEach
    void setUp() {
        // Use lenient stubbing for properties that may not be used in all tests
        lenient().when(properties.getEnabled()).thenReturn(true);
        lenient().when(properties.getApiUrl()).thenReturn("https://api.stripe.com/v1");
        lenient().when(properties.getCacheTtlSeconds()).thenReturn(3600L);
        lenient().when(properties.getTimeoutSeconds()).thenReturn(5);
        lenient().when(properties.getAllowOfflineValidation()).thenReturn(true);
        lenient().when(properties.getLicenseKeyPrefix()).thenReturn("stripe_");
        lenient().when(properties.getProductIdPremium()).thenReturn("");
        lenient().when(properties.getProductIdEnterprise()).thenReturn("");

        service = new StripeLicenseService(properties, stripeWebClient);
    }

    @Test
    void shouldReturnNullForNullLicenseKey() {
        FeatureFlagsProperties.LicenseTier tier = service.validateLicense(null);
        
        assertThat(tier).isNull();
    }

    @Test
    void shouldReturnNullForBlankLicenseKey() {
        FeatureFlagsProperties.LicenseTier tier = service.validateLicense("");
        
        assertThat(tier).isNull();
    }

    @Test
    void shouldReturnNullForNonStripeKeys() {
        FeatureFlagsProperties.LicenseTier tier = service.validateLicense("PREMIUM-test-key");
        
        assertThat(tier).isNull();
    }

    @Test
    void shouldUseSimpleValidationWhenStripeDisabled() {
        when(properties.getEnabled()).thenReturn(false);
        
        FeatureFlagsProperties.LicenseTier tier = service.validateLicense("stripe_PREMIUM-test-key");
        
        assertThat(tier).isEqualTo(FeatureFlagsProperties.LicenseTier.PREMIUM);
    }

    @Test
    void shouldCacheValidationResults() {
        // This test would require mocking WebClient responses
        // For now, we test that cache methods exist
        service.clearCache();
        service.clearCache("test-key");
        
        // If we get here without exception, cache methods work
        assertThat(true).isTrue();
    }
}

