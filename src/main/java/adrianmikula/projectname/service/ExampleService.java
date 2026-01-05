package adrianmikula.projectname.service;

import adrianmikula.projectname.dao.ExampleRepository;
import adrianmikula.projectname.entity.ExampleEntity;
import adrianmikula.projectname.rest.Example;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Example service bean.
 * This is a template example - replace with your own domain logic.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExampleService {
    
    private final ExampleRepository exampleRepository;
    private final ExampleMapper exampleMapper;
    
    /**
     * Example service method - create a new example.
     */
    @Transactional
    public ExampleEntity create(Example example) {
        log.info("Creating example: {}", example.getName());
        
        if (example.getName() == null || example.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Name is required");
        }
        
        if (exampleRepository.existsByName(example.getName())) {
            throw new IllegalArgumentException("Example with this name already exists: " + example.getName());
        }
        
        ExampleEntity entity = exampleMapper.toEntity(example);
        return exampleRepository.save(entity);
    }
    
    /**
     * Example service method - get all examples.
     */
    public List<Example> getAll() {
        return exampleRepository.findAll().stream()
                .map(exampleMapper::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * Example service method - get example by ID.
     */
    public Optional<Example> getById(UUID id) {
        return exampleRepository.findById(id)
                .map(exampleMapper::toDomain);
    }
}

