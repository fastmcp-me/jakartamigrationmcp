package adrianmikula.projectname.service;

import adrianmikula.projectname.entity.ExampleEntity;
import adrianmikula.projectname.rest.Example;
import org.springframework.stereotype.Component;

/**
 * Example mapper for converting between domain and entity.
 * This is a template example - replace with your own mappers.
 */
@Component
public class ExampleMapper {

    public ExampleEntity toEntity(Example domain) {
        if (domain == null) {
            return null;
        }

        return ExampleEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .description(domain.getDescription())
                .category(domain.getCategory())
                .status(domain.getStatus())
                .build();
    }

    public Example toDomain(ExampleEntity entity) {
        if (entity == null) {
            return null;
        }

        return Example.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(entity.getCategory())
                .status(entity.getStatus())
                .build();
    }
}

