package adrianmikula.projectname.e2e;

import adrianmikula.projectname.component.AbstractComponentTest;
import adrianmikula.projectname.dao.ExampleRepository;
import adrianmikula.projectname.entity.ExampleEntity;
import adrianmikula.projectname.rest.Example;
import adrianmikula.projectname.service.ExampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Example end-to-end test.
 * This is a template example - replace with your own E2E tests.
 */
@DisplayName("Example E2E Test")
class ExampleE2ETest extends AbstractComponentTest {

    @Autowired
    private ExampleRepository exampleRepository;

    @Autowired
    private ExampleService exampleService;

    @BeforeEach
    void setUp() {
        exampleRepository.deleteAll();
    }

    @Test
    @DisplayName("Should create example and retrieve it end-to-end")
    @Transactional
    void shouldCreateAndRetrieveExampleE2E() {
        // Given
        String name = "Test Example";
        Example example = Example.builder()
                .name(name)
                .description("Test Description")
                .category("Test Category")
                .status("ACTIVE")
                .build();

        // When - Create via service
        ExampleEntity saved = exampleService.create(example);

        // Then - Verify via repository
        assertNotNull(saved.getId());
        var retrieved = exampleRepository.findById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals(name, retrieved.get().getName());
    }
}

