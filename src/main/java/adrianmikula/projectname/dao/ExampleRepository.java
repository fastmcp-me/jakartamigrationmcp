package adrianmikula.projectname.dao;

import adrianmikula.projectname.entity.ExampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Example Spring Data JPA repository (DAO).
 * This is a template example - replace with your own domain entities.
 */
@Repository
public interface ExampleRepository extends JpaRepository<ExampleEntity, UUID> {
    
    /**
     * Example custom query method - Spring Data JPA will implement this automatically.
     */
    boolean existsByName(String name);
    
    /**
     * Example custom query method - Spring Data JPA will implement this automatically.
     */
    Optional<ExampleEntity> findByName(String name);
}

