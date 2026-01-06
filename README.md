# Jakarta Migration MCP Server

A Model Context Protocol (MCP) server that provides AI assistants with specialized tools for analyzing and migrating Java applications from Java EE 8 (`javax.*`) to Jakarta EE 9+ (`jakarta.*`). This tool helps automate the complex process of Jakarta migration by providing deep dependency analysis, blocker detection, version recommendations, and migration planning.

## What It Does

The Jakarta Migration MCP Server enables AI coding assistants (Cursor, Claude Code, Antigravity) to:

- **Analyze Jakarta Readiness** - Assess Java projects for migration readiness with detailed dependency analysis
- **Detect Blockers** - Identify dependencies and code patterns that prevent Jakarta migration
- **Recommend Versions** - Suggest Jakarta-compatible versions for existing dependencies
- **Create Migration Plans** - Generate comprehensive, phased migration plans with risk assessment
- **Verify Runtime** - Test migrated applications to ensure they run correctly after migration

### The Problem It Solves

Migrating from Java EE 8 (`javax.*`) to Jakarta EE 9+ (`jakarta.*`) is complex because:
- **Dependency Hell**: Many libraries haven't migrated, creating transitive conflicts
- **Binary Incompatibility**: Compiled JARs may reference `javax.*` internally
- **Hidden Dependencies**: `javax.*` usage in XML configs, annotations, and dynamic loading
- **Risk Assessment**: Need to understand migration impact before starting

This MCP server provides AI assistants with the specialized knowledge and tools to navigate these challenges effectively.

## Features

### Core Capabilities

- **Deep Dependency Analysis** - Analyzes Maven/Gradle projects to build complete dependency graphs
- **Namespace Classification** - Identifies `javax.*` vs `jakarta.*` dependencies automatically
- **Blocker Detection** - Finds dependencies with no Jakarta equivalents
- **Version Recommendations** - Suggests Jakarta-compatible versions with migration paths
- **Migration Planning** - Creates phased migration plans with risk assessment
- **Runtime Verification** - Tests migrated applications to catch runtime issues
- **OpenRewrite Integration** - Uses OpenRewrite for automated code refactoring

### Premium Features (with License)

- **Auto-Fixes** - Automatically fix detected issues without manual intervention
- **One-Click Refactor** - Execute complete Jakarta migration with a single command
- **Binary Fixes** - Fix issues in compiled binaries and JAR files
- **Advanced Analysis** - Deep transitive conflict detection and resolution
- **Batch Operations** - Process multiple projects simultaneously
- **Custom Recipes** - Create and use custom migration recipes
- **API Access** - Programmatic API for CI/CD integrations
- **Export Reports** - Export detailed reports in PDF, HTML formats

## Tech Stack

### Core Technologies

- **Java 21** - Modern Java with virtual threads and pattern matching
- **Spring Boot 3.2+** - Application framework with Spring AI MCP integration
- **Spring AI 1.0.0** - MCP server framework and AI integration
- **Gradle** - Build automation and dependency management
- **OpenRewrite** - Automated code refactoring and migration recipes

### Key Libraries

- **JGit** - Git repository operations
- **ASM** - Bytecode analysis for runtime verification
- **Resilience4j** - Circuit breakers and rate limiting
- **TestContainers** - Integration testing with Docker
- **JaCoCo** - Code coverage reporting

### Infrastructure

- **PostgreSQL** - State management (optional, for advanced features)
- **Redis** - Caching and queues (optional)
- **Docker Compose** - Local development environment

## Quick Start

### Prerequisites

- **Java 21+** - [Download from Adoptium](https://adoptium.net/)
- **Node.js 18+** - [Download from nodejs.org](https://nodejs.org/)
- **Docker & Docker Compose** - For local services (optional)

### Using Mise (Recommended)

[mise](https://mise.jdx.dev/) is a tool version manager that handles all dependencies automatically.

**Install mise:**
```bash
# Windows (winget)
winget install jdx.mise

# Linux/Mac
curl https://mise.run | sh

# Mac (Homebrew)
brew install mise
```

**Setup project:**
```bash
cd JakartaMigrationMCP
mise install          # Installs Java 21, Gradle, Node.js
mise run setup        # Runs setup script
```

**Common mise commands:**
```bash
mise tasks            # View all available commands
mise run test         # Run all tests
mise run test-unit    # Run unit tests only
mise run build        # Build the project (without tests)
mise run build-all    # Build with tests
mise run run          # Run the application
mise run coverage     # Generate code coverage report
mise run clean        # Clean build artifacts
mise run start-services # Start Docker services (PostgreSQL, Redis)
```

See [Mise Setup Guide](docs/setup/MISE_SETUP.md) for complete task reference.

See [Mise Setup Guide](docs/setup/MISE_SETUP.md) for detailed instructions.

### Manual Setup

**1. Install Java 21+**
```bash
# Verify installation
java -version
```

**2. Install Node.js 18+**
```bash
# Verify installation
node --version
npm --version
```

**3. Build the project**
```bash
./gradlew build
```

**4. Run the application**
```bash
./gradlew bootRun
```

## MCP Installation & Usage

### Quick Install

```bash
# Install globally via npm
npm install -g @jakarta-migration/mcp-server

# Or use with npx (no installation needed)
npx -y @jakarta-migration/mcp-server
```

### Configure in MCP Clients

#### Cursor

1. Open Cursor Settings (`Ctrl+,` or `Cmd+,`)
2. Navigate to **Features** → **MCP**
3. Click **"+ Add New MCP Server"**
4. Add configuration:

```json
{
  "mcpServers": {
    "jakarta-migration": {
      "command": "npx",
      "args": ["-y", "@jakarta-migration/mcp-server"]
    }
  }
}
```

5. **Restart Cursor** for changes to take effect

#### Claude Code

1. Open Claude Code Settings
2. Navigate to **MCP** settings
3. Add the same configuration as above
4. Restart Claude Code

#### Google Antigravity

Add the configuration to your Antigravity MCP settings file (location may vary).

### Usage Examples

Once configured, you can use the MCP tools in your AI assistant:

```
Analyze the Jakarta readiness of my project at /path/to/project
```

```
Detect any blockers for Jakarta migration in my project
```

```
Create a migration plan for migrating to Jakarta EE
```

```
Recommend Jakarta-compatible versions for my dependencies
```

```
Verify the runtime of my migrated application
```

### Available MCP Tools

- **`analyzeJakartaReadiness`** - Comprehensive project analysis
- **`detectBlockers`** - Find migration blockers
- **`recommendVersions`** - Get version recommendations
- **`createMigrationPlan`** - Generate migration plan
- **`verifyRuntime`** - Test migrated application

See [MCP Tools Documentation](docs/mcp/MCP_TOOLS_IMPLEMENTATION.md) for detailed tool descriptions.

## Project Structure

```
src/
├── main/java/adrianmikula/jakartamigration/
│   ├── config/              # Configuration (feature flags, license validation)
│   ├── mcp/                 # MCP tools implementation
│   ├── dependencyanalysis/  # Dependency analysis module
│   ├── coderefactoring/     # Code refactoring module
│   └── runtimeverification/ # Runtime verification module
└── test/
    ├── java/unit/           # Unit tests
    ├── java/component/      # Component tests
    └── java/e2e/            # End-to-end tests
```

## Documentation

### Setup & Installation

- **[Installation Guide](docs/setup/INSTALLATION.md)** - Complete installation instructions
- **[Mise Setup](docs/setup/MISE_SETUP.md)** - Tool version management with mise
- **[MCP Setup](docs/setup/MCP_SETUP.md)** - MCP server configuration
- **[Feature Flags Setup](docs/setup/FEATURE_FLAGS_SETUP.md)** - License and feature configuration
- **[Stripe License Setup](docs/setup/STRIPE_LICENSE_SETUP.md)** - Stripe subscription validation
- **[Apify License Setup](docs/setup/APIFY_LICENSE_SETUP.md)** - Apify license validation

### Architecture

- **[Core Modules Design](docs/architecture/core-modules-design.md)** - System architecture
- **[Feature Flags](docs/architecture/FEATURE_FLAGS.md)** - Feature flag system
- **[MCP Tools](docs/mcp/MCP_TOOLS_IMPLEMENTATION.md)** - Tool implementation details

### Development

- **[Coding Standards](docs/standards/README.md)** - Code quality guidelines
- **[Testing Guide](docs/testing/README.md)** - Testing standards and practices
- **[Packaging Guide](docs/setup/PACKAGING.md)** - Build and release process

## Development

### Running Tests

```bash
# All tests
./gradlew test

# Unit tests only
./gradlew test --tests "*Test" --exclude-tests "*ComponentTest" --exclude-tests "*E2ETest"

# Component tests
./gradlew test --tests "adrianmikula.jakartamigration.component.*"

# With coverage
./gradlew test jacocoTestReport
```

### Building

```bash
# Build JAR
./gradlew bootJar

# Build for release
./scripts/build-release.sh  # Linux/macOS
.\scripts\build-release.ps1  # Windows
```

### Code Coverage

Coverage reports are generated automatically after tests:

- **HTML Report**: `build/reports/jacoco/test/html/index.html`
- **XML Report**: `build/reports/jacoco/test/jacocoTestReport.xml`

```bash
# Generate coverage
./gradlew jacocoTestReport

# View coverage summary
./gradlew jacocoCoverageSummary
```

## License & Monetization

This project uses a hybrid licensing model:

- **Community Tier** - Free, open-source version with basic features
- **Premium Tier** - Advanced features (auto-fixes, one-click refactor, binary fixes)
- **Enterprise Tier** - Priority support, cloud hosting, SLA guarantees

License validation supports:
- **Stripe** - Subscription-based licenses
- **Apify** - Usage-based billing
- **Test Keys** - For development and testing

See [Monetization Research](docs/research/monetisation.md) for details.

## Contributing

Contributions are welcome! Please see the [Contributing Guide](CONTRIBUTING.md) for details.

## License

MIT

## Resources

- **MCP Documentation**: https://modelcontextprotocol.io
- **Spring AI**: https://docs.spring.io/spring-ai/reference/
- **OpenRewrite**: https://docs.openrewrite.org/
- **Jakarta EE**: https://jakarta.ee/

---

**Built with ❤️ for the Java community**
