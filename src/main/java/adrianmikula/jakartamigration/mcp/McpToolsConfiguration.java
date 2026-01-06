package adrianmikula.jakartamigration.mcp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.mcp.server.ToolCallbackProvider;
import org.springframework.ai.mcp.server.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for MCP Tools registration.
 * 
 * This configuration manually registers @McpTool annotated methods as a workaround
 * for the known issue where annotation scanner might not automatically register tools
 * in Spring AI 1.1.2.
 * 
 * Reference: https://github.com/spring-projects/spring-ai/issues/4392
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class McpToolsConfiguration {
    
    private final JakartaMigrationTools jakartaMigrationTools;
    
    /**
     * Manually registers JakartaMigrationTools methods as MCP tools.
     * 
     * This is a workaround for the annotation scanner issue where @McpTool
     * annotated methods might not be automatically registered.
     * 
     * @return ToolCallbackProvider that provides access to all Jakarta migration tools
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider() {
        log.info("Registering Jakarta Migration MCP tools manually via ToolCallbackProvider");
        
        try {
            ToolCallbackProvider provider = MethodToolCallbackProvider.builder()
                .toolObjects(jakartaMigrationTools)
                .build();
            
            log.info("Successfully registered Jakarta Migration MCP tools");
            return provider;
        } catch (Exception e) {
            log.error("Failed to register MCP tools manually", e);
            // Return a no-op provider if registration fails
            // This allows the application to start even if MCP registration fails
            return MethodToolCallbackProvider.builder().build();
        }
    }
}

