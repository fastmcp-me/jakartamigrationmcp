package adrianmikula.projectname.component;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;

/**
 * Test configuration for component tests using TestContainers.
 * Containers are managed by AbstractComponentTest via @Container annotations.
 * 
 * This is a template example - add your own test configuration here.
 */
@TestConfiguration
@Profile("component-test")
public class ComponentTestConfiguration {
    // Add test-specific beans here if needed
}

