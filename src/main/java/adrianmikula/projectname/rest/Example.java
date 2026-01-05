package adrianmikula.projectname.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Example domain model.
 * This is a template example - replace with your own domain models.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Example {
    
    @Builder.Default
    private UUID id = UUID.randomUUID();
    
    private String name;
    private String description;
    private String category;
    private String status;
}

