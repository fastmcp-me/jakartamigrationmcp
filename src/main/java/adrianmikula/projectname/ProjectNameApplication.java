package adrianmikula.projectname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Spring Boot application main class.
 * This is a template - replace with your own application name and configuration.
 */
@SpringBootApplication
@EnableScheduling
public class ProjectNameApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectNameApplication.class, args);
    }
}

