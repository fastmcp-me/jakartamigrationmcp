# Spring Boot Quick-Start Template

A modern Spring Boot 3.x application template with best practices, comprehensive testing, and production-ready configuration.

**This is a quick-start template** - Use this as a foundation for your new Spring Boot projects. It includes a complete setup with testing infrastructure, code quality standards, and documentation to help you get started quickly.

## Features

- **Modern Tech Stack**: Spring Boot 3.2+, Java 21, Gradle
- **Reactive Programming**: Spring WebFlux with Project Reactor
- **Database Support**: PostgreSQL with Liquibase migrations
- **Caching & Queues**: Redis integration
- **LLM Integration**: Optional Ollama integration via Spring AI
- **Virtual Threads**: High concurrency with Project Loom (Java 21)
- **Comprehensive Testing**: Unit tests, component tests with TestContainers
- **Code Quality**: Pre-configured with code quality standards and best practices
- **Docker Support**: Docker Compose for local development
- **Git Operations**: JGit integration for repository operations
- **Resilience Patterns**: Circuit breakers and rate limiting with Resilience4j
- **TDD Approach**: Test-driven development with comprehensive test coverage

## Tech Stack

- **Spring Boot 3.2+** with Java 21
- **Gradle** for build automation
- **Spring AI** for LLM orchestration
- **PostgreSQL** for state management
- **Redis** for caching and queues
- **JGit** for Git operations
- **Ollama** for local LLM inference
- **Resilience4j** for circuit breakers and rate limiting
- **Docker Compose** for local development

## Project Structure

```
src/
├── main/
│   ├── java/com/yourproject/
│   │   ├── domain/          # Domain models and services
│   │   ├── entity/          # JPA entities
│   │   ├── repository/      # Data access layer
│   │   ├── service/         # Business logic
│   │   ├── mapper/          # Domain-entity mapping
│   │   ├── config/          # Configuration classes
│   │   └── web/             # Controllers and web layer
│   └── resources/
│       └── application.yml
└── test/
    ├── java/unit/           # Unit tests
    └── java/component/      # Component tests (TestContainers)
```

## Documentation

### Architecture

For detailed architecture documentation, see the [Architecture Documentation](docs/architecture/README.md) which covers:

- **Architectural Patterns**: Reactive programming, DDD, circuit breakers, event-driven architecture
- **System Components**: Domain layers, services, and component interactions
- **Technology Stack**: Framework choices, database patterns, and integrations
- **Design Principles**: Domain-driven design, service layer patterns, and best practices

### Coding Standards

This project follows industry-standard best practices. See the [Coding Standards](docs/standards/README.md) for:

- **Core Principles**: TDD, DRY, KISS, SOLID
- **Testing Standards**: Unit tests, component tests, coverage requirements
- **Code Quality**: Code review checklist, design patterns, best practices
- **Java 21 Features**: Modern language features and usage guidelines
- **Common Gotchas**: [Common pitfalls and problems](docs/standards/common-gotchas.md) to avoid - includes Spring Boot test configuration, reactive programming pitfalls, and more

### MCP Servers Setup

Enhance your AI coding workflow with Model Context Protocol servers. See the [MCP Setup Guide](docs/setup/MCP_SETUP.md) for:

- **Code Indexing**: Fast semantic codebase search (60-80% token savings)
- **Memory Storage**: Long-term context across sessions (20-40% token savings)
- **Spring Boot Monitoring**: Real-time logs, health, and metrics (70-90% token savings)
- **Build Tool Integration**: Gradle and npm dependency analysis

## Getting Started

### Prerequisites

- Java 21+
- Gradle 8.0+ (or use included wrapper)
- Docker & Docker Compose
- Ollama (for local LLM)

### Quick Setup

**Linux/Mac:**
```bash
./scripts/setup.sh
```

**Windows (PowerShell):**
```powershell
.\scripts\setup.ps1
```

The setup script will:
- Install mise-en-place if not present
- Use mise to install tools (Java 21, Gradle 8.5)
- Start PostgreSQL and Redis via Docker
- Create configuration files and directories
- Build the project

### Configuration

Before running the application, configure any required API keys or secrets:

1. **Copy the example environment file:**
   ```bash
   cp .env.example .env
   ```

2. **Configure API keys** - See [Manual Setup Guide](docs/setup/MANUAL_SETUP.md) for detailed instructions on setting up:
   - GitHub API tokens (if using GitHub integration)
   - Webhook secrets (if using webhooks)
   - External API keys (as needed for your application)
   - Ollama configuration (if using LLM features)

3. **Edit `.env` file** with your actual configuration values

For full setup instructions, see [Manual Setup Guide](docs/setup/MANUAL_SETUP.md).

After setup, use mise commands for daily development:
```bash
mise run test            # Run tests
mise run run             # Run backend application
mise run frontend-dev    # Start frontend development server
mise run frontend-build  # Build frontend for production
mise tasks               # View all commands
```

### Manual Setup

1. **Start Docker services:**
   ```bash
   docker compose up -d
   ```

2. **Install Ollama (if using LLM features):**
   ```bash
   # Linux/Mac
   curl -fsSL https://ollama.ai/install.sh | sh
   
   # Windows: Download from https://ollama.ai
   
   # Pull a model (example)
   ollama pull deepseek-coder:6.7b
   ```

3. **Initialize Gradle wrapper:**
   ```bash
   gradle wrapper
   ```

4. **Build the project:**
   ```bash
   ./gradlew build
   ```

5. **Run the application:**
   ```bash
   ./gradlew bootRun
   ```

### Code Coverage

Code coverage reports are automatically generated after each test run using JaCoCo.

**Using Mise (Recommended):**
```bash
mise run test          # Run tests (automatically generates coverage)
mise run coverage      # Generate coverage report manually
mise run coverage-open # Open coverage report in browser
mise run coverage-clean # Clean coverage reports
```

**Using Gradle directly:**
```bash
# After running tests, coverage reports are available at:
# HTML: build/reports/jacoco/test/html/index.html
# XML:  build/reports/jacoco/test/jacocoTestReport.xml

# Generate coverage report manually:
./gradlew jacocoTestReport

# Reports are also saved with timestamps for historical tracking:
# build/reports/jacoco-html-YYYY-MM-DD_HH-mm-ss/
```

**Coverage configuration:**
- Excludes: Config classes, entities, DTOs, and application main class
- Formats: HTML (interactive) and XML (for CI/CD integration)
- Historical tracking: Timestamped reports saved for every test run

### Managing Services

**Start services:**
- Linux/Mac: `./scripts/start-services.sh`
- Windows: `.\scripts\start-services.ps1`

**Stop services:**
- Linux/Mac: `./scripts/stop-services.sh`
- Windows: `.\scripts\stop-services.ps1`

## Running Tests

### Unit Tests
```bash
./gradlew test
```

### Component Tests (Integration Tests)
Component tests use Spring Boot Test with TestContainers to test major features in a real containerized environment:

```bash
# Run all tests including component tests
./gradlew test

# Run only component tests
./gradlew test --tests "com.yourproject.component.*"

# Run specific component test
./gradlew test --tests "com.yourproject.component.DataPollingComponentTest"
```

**Component Test Examples:**
- End-to-end workflow tests with real databases
- Redis queue operations
- LLM integration tests (with mocked clients)
- HTTP client integration tests
- Repository service tests

**Note:** Component tests require Docker to be running for TestContainers. See [Component Tests Guide](docs/testing/COMPONENT_TESTS.md) for details.

## Command Reference

### Using Mise (Recommended)

If you have [mise](https://mise.jdx.dev/) installed:

```bash
mise install          # Install tools and setup
mise tasks            # View all available commands
mise run setup        # Run setup
mise run test         # Run tests (generates coverage automatically)
mise run coverage      # Generate coverage report
mise run coverage-open # Open coverage report in browser
mise run run          # Run application
```

See `MISE_SETUP.md` for detailed mise setup instructions.

### Direct Commands

See `COMMANDS.md` for a complete catalog of all available commands, scripts, and workflows.

## API Endpoints

The template includes example REST API endpoints. Customize these for your application:

- `GET /actuator/health` - Health check endpoint
- `GET /actuator/info` - Application information

Add your own endpoints in the `web/controller` package following the existing patterns.

## Building

```bash
# Build without tests
./gradlew build -x test

# Build with tests
./gradlew build

# Create executable JAR
./gradlew bootJar
```

## Packaging and Distribution

This project is distributed as an npm package for easy installation with MCP clients.

### Quick Install

```bash
# Install globally
npm install -g @jakarta-migration/mcp-server

# Or use with npx (no installation)
npx -y @jakarta-migration/mcp-server
```

### Building for Release

**Linux/macOS:**
```bash
./scripts/build-release.sh
```

**Windows:**
```powershell
.\scripts\build-release.ps1
```

This creates release artifacts in the `release/` directory.

### Publishing

See [Packaging Guide](docs/setup/PACKAGING.md) for detailed release instructions.

For installation in MCP clients (Cursor, Claude Code, Antigravity), see [Installation Guide](docs/setup/INSTALLATION.md).

## Next Steps

After cloning this template:

1. **Update package names**: Replace `com.yourproject` with your actual package name
2. **Customize domain models**: Create your own domain entities and services
3. **Configure API keys**: Set up any external API integrations you need
4. **Add your features**: Build on top of the existing infrastructure
5. **Review documentation**: Check the [docs](docs/) folder for detailed guides

## Template Features

This template provides:

- ✅ **Complete project structure** with best practices
- ✅ **Testing infrastructure** (unit tests, component tests with TestContainers)
- ✅ **Code quality tools** and standards
- ✅ **Docker Compose** setup for local development
- ✅ **Comprehensive documentation** for setup, testing, and development
- ✅ **Modern Java features** (Java 21, virtual threads, records, pattern matching)
- ✅ **Reactive programming** support with Spring WebFlux
- ✅ **Database migrations** with Liquibase
- ✅ **Code coverage** reporting with JaCoCo

## Contributing

This is a template repository. Feel free to fork and customize it for your projects.

## License

MIT

---

**Note**: This is a quick-start template. Customize it to fit your specific project needs. Remove or modify any features that don't apply to your use case.


