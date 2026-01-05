package adrianmikula.projectname.component;

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
 * Example component test using Spring Boot Test and TestContainers.
 * This is a template example - replace with your own component tests.
 */
@DisplayName("Example Service Component Tests")
class ExampleServiceComponentTest extends AbstractComponentTest {

    @Autowired
    private ExampleService exampleService;

    @Autowired
    private ExampleRepository exampleRepository;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        exampleRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save and retrieve example from database")
    @Transactional
    void shouldSaveAndRetrieveExample() {
        // Given
        Example example = Example.builder()
                .name("Test Example")
                .description("Test Description")
                .category("Test Category")
                .status("ACTIVE")
                .build();

        // When
        ExampleEntity saved = exampleService.create(example);
        var retrieved = exampleService.getById(saved.getId());

        // Then
        assertNotNull(saved);
        assertTrue(retrieved.isPresent());
        assertEquals(example.getName(), retrieved.get().getName());
    }
}

