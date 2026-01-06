package adrianmikula.projectname;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

/**
 * Spring Boot application main class.
 * 
 * For MCP stdio mode: stdout must ONLY contain JSON-RPC messages.
 * Spring Boot banner is disabled, and logging is configured to use stderr.
 * 
 * Database and JPA auto-configuration are excluded as they're not needed for the MCP server.
 */
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    LiquibaseAutoConfiguration.class
})
@EnableScheduling
public class ProjectNameApplication {

    public static void main(String[] args) {
        // Check if we're running in MCP stdio mode
        boolean isStdioMode = Arrays.asList(args).contains("--spring.profiles.active=mcp-stdio") ||
                             Arrays.asList(args).contains("--spring.ai.mcp.server.transport=stdio") ||
                             "stdio".equals(System.getenv("MCP_TRANSPORT")) ||
                             System.getProperty("spring.profiles.active", "").contains("mcp-stdio");
        
        SpringApplication app = new SpringApplication(ProjectNameApplication.class);
        
        if (isStdioMode) {
            // Disable banner - it would pollute stdout and break JSON-RPC protocol
            // The banner is ASCII art that gets printed to stdout before MCP server starts
            app.setBannerMode(org.springframework.boot.Banner.Mode.OFF);
        }
        
        app.run(args);
    }
}

