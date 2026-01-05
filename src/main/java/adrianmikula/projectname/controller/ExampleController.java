package adrianmikula.projectname.controller;

import adrianmikula.projectname.rest.CreateExampleRequest;
import adrianmikula.projectname.rest.ErrorResponse;
import adrianmikula.projectname.rest.Example;
import adrianmikula.projectname.entity.ExampleEntity;
import adrianmikula.projectname.service.ExampleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Example REST controller.
 * This is a template example - replace with your own domain logic.
 */
@RestController
@RequestMapping("/api/examples")
@RequiredArgsConstructor
@Slf4j
public class ExampleController {
    
    private final ExampleService exampleService;
    
    /**
     * Example POST endpoint - create a new example.
     */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateExampleRequest request) {
        log.info("Received request to create example: {}", request.getName());
        
        try {
            Example example = Example.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .category(request.getCategory())
                    .status("ACTIVE")
                    .build();
            
            ExampleEntity saved = exampleService.create(example);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            log.warn("Failed to create example: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }
    
    /**
     * Example GET endpoint - get all examples.
     */
    @GetMapping
    public ResponseEntity<List<Example>> getAll() {
        log.debug("Fetching all examples");
        List<Example> examples = exampleService.getAll();
        return ResponseEntity.ok(examples);
    }
    
    /**
     * Example GET endpoint with path variable - get example by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Example> getById(@PathVariable UUID id) {
        log.debug("Fetching example by ID: {}", id);
        return exampleService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Example exception handler for validation errors.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }
}

