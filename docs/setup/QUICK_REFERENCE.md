# Quick Reference: MCP Server Development Setup

This is a quick reference guide for setting up your development environment for building the Jakarta Migration MCP server.

## ✅ What's Already Set Up

### 1. OpenRewrite ✅
- **Status**: Added to both `pom.xml` and `build.gradle.kts`
- **Version**: 8.10.0
- **Recipes**: Jakarta migration recipes pre-configured
- **Usage**: 
  - Maven: `mvn rewrite:dryRun` or `mvn rewrite:run`
  - Gradle: `./gradlew rewriteDryRun` or `./gradlew rewriteRun`

### 2. Spring AI MCP Server ✅
- **Status**: Dependency added (ready to use)
- **Version**: Matches your Spring AI version
- **Usage**: Just add `@Tool` annotations to create MCP tools

### 3. Java Development Tools ✅
- **Java 21**: Configured via `mise` (`.mise.toml`)
- **Gradle 8.5**: Configured
- **Maven**: Available

## 📚 Documentation

| Document | Purpose |
|----------|---------|
| [`DEV_ENV_SETUP.md`](./DEV_ENV_SETUP.md) | Complete dev environment setup guide |
| [`JAVA_MCP_LIBRARIES.md`](./JAVA_MCP_LIBRARIES.md) | Java MCP server building blocks |
| [`../mcp-servers-recommendations.md`](../mcp-servers-recommendations.md) | Recommended MCP servers to use |

## 🚀 Quick Start: Building Your First MCP Tool

### Step 1: Create a Tool Component

```java
package com.jakartamigration.mcp;

import org.springframework.ai.mcp.server.Tool;
import org.springframework.stereotype.Component;

@Component
public class JakartaMigrationTools {
    
    @Tool(
        name = "analyze_jakarta_readiness",
        description = "Analyzes a Java project for Jakarta migration readiness"
    )
    public String analyzeReadiness(String projectPath) {
        // Your implementation using OpenRewrite
        return "Analysis result...";
    }
}
```

### Step 2: Configure MCP Server

Add to `application.yml`:

```yaml
spring:
  ai:
    mcp:
      server:
        name: jakarta-migration-mcp
        version: 1.0.0
        transport: stdio  # or 'sse' for HTTP
```

### Step 3: Run

```bash
./gradlew bootRun
# Or
mvn spring-boot:run
```

## 🔧 Available MCP Server Libraries

| Library | Best For | Status |
|---------|----------|--------|
| **Spring AI MCP Server** | Spring Boot projects (this project!) | ✅ Added |
| **Official MCP Java SDK** | Framework-agnostic | 📝 Documented |
| **Quarkus MCP SDK** | Quarkus projects | 📝 Documented |

See [`JAVA_MCP_LIBRARIES.md`](./JAVA_MCP_LIBRARIES.md) for details.

## 🛠️ Useful Commands

### OpenRewrite

```bash
# Preview changes (dry run)
mvn rewrite:dryRun
./gradlew rewriteDryRun

# Apply changes
mvn rewrite:run
./gradlew rewriteRun

# Discover available recipes
mvn rewrite:discover
./gradlew rewriteDiscover
```

### Build & Test

```bash
# Build
./gradlew build
mvn clean package

# Test
./gradlew test
mvn test

# Run
./gradlew bootRun
mvn spring-boot:run
```

### Dependency Analysis

```bash
# Maven
mvn dependency:tree
mvn dependency:analyze

# Gradle
./gradlew dependencies
./gradlew dependencyInsight --dependency <groupId>:<artifactId>
```

## 📦 Recommended MCP Servers to Install

1. **ArchLift Java Modernization MCP** - Jakarta migration with OpenRewrite
   ```bash
   npm install -g @archlift/remodern-java-mcp
   ```

2. **Maven Tools MCP** - Dependency analysis
   ```bash
   npm install -g maven-tools-mcp
   ```

See [`../mcp-servers-recommendations.md`](../mcp-servers-recommendations.md) for full list.

## 🎯 Next Steps

1. ✅ **Done**: OpenRewrite installed
2. ✅ **Done**: Spring AI MCP Server dependency added
3. 📝 **Next**: Create your first MCP tool (see Quick Start above)
4. 📝 **Next**: Integrate OpenRewrite in tool implementations
5. 📝 **Next**: Test MCP server with Cursor/Claude Desktop

## 🔗 Key Resources

- **OpenRewrite Docs**: https://docs.openrewrite.org
- **Spring AI Docs**: https://docs.spring.io/spring-ai/reference/
- **MCP Protocol**: https://modelcontextprotocol.io
- **Official MCP Java SDK**: https://github.com/modelcontextprotocol/java-sdk

---

*For detailed information, see the full documentation files.*

