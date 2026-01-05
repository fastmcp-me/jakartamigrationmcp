package adrianmikula.projectname.unit;

import adrianmikula.projectname.dao.ExampleRepository;
import adrianmikula.projectname.entity.ExampleEntity;
import adrianmikula.projectname.rest.Example;
import adrianmikula.projectname.service.ExampleMapper;
import adrianmikula.projectname.service.ExampleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Example unit test using JUnit and Mockito.
 * This is a template example - replace with your own unit tests.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ExampleService Unit Tests")
class ExampleServiceTest {

    @Mock
    private ExampleRepository exampleRepository;

    @Mock
    private ExampleMapper exampleMapper;

    @InjectMocks
    private ExampleService exampleService;

    private Example testExample;
    private ExampleEntity testEntity;

    @BeforeEach
    void setUp() {
        UUID testId = UUID.randomUUID();
        testExample = Example.builder()
                .id(testId)
                .name("Test Example")
                .description("Test Description")
                .category("Test Category")
                .build();

        testEntity = ExampleEntity.builder()
                .id(testId)
                .name("Test Example")
                .description("Test Description")
                .category("Test Category")
                .build();
    }

    @Test
    @DisplayName("Should create example successfully")
    void shouldCreateExample() {
        // Given
        when(exampleRepository.existsByName(anyString())).thenReturn(false);
        when(exampleMapper.toEntity(any(Example.class))).thenReturn(testEntity);
        when(exampleRepository.save(any(ExampleEntity.class))).thenReturn(testEntity);

        // When
        ExampleEntity result = exampleService.create(testExample);

        // Then
        assertNotNull(result);
        assertEquals(testEntity.getName(), result.getName());
        verify(exampleRepository, times(1)).save(any(ExampleEntity.class));
    }

    @Test
    @DisplayName("Should get all examples")
    void shouldGetAllExamples() {
        // Given
        when(exampleRepository.findAll()).thenReturn(List.of(testEntity));
        when(exampleMapper.toDomain(any(ExampleEntity.class))).thenReturn(testExample);

        // When
        List<Example> result = exampleService.getAll();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(exampleRepository, times(1)).findAll();
    }
}

