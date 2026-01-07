# Jakarta Migration MCP Server - JSON Schemas

This directory contains JSON Schema definitions for the Jakarta Migration MCP Server's input and output formats.

## Files

### MCP Client Schemas (JSON Schema Draft 7)

- **`mcp-input-schemas.json`** - Input parameter schemas for all MCP tools (for MCP client validation)
- **`mcp-output-schemas.json`** - Output response schemas for all MCP tools (for MCP client validation)
- **`example-requests-responses.json`** - Example request/response pairs

### Apify Actor Schemas

- **`.actor/input_schema.json`** - Apify input schema for Actor UI generation
- **`.actor/output_schema.json`** - Apify output schema for output display
- **`.actor/actor.json`** - Actor metadata and configuration

See [APIFY_SCHEMAS.md](APIFY_SCHEMAS.md) for details on Apify-specific schemas.

## Usage

These schemas can be used for:

1. **API Documentation** - Generate OpenAPI/Swagger documentation
2. **Validation** - Validate input/output in client applications
3. **Type Generation** - Generate TypeScript/Java types from schemas
4. **Testing** - Validate test data against schemas
5. **Integration** - Understand expected formats for MCP client integration

## Schema Structure

### Input Schemas

Each tool has a corresponding input schema definition:

```json
{
  "toolInputs": {
    "analyzeJakartaReadiness": {
      "type": "object",
      "required": ["projectPath"],
      "properties": {
        "projectPath": {
          "type": "string",
          "description": "Path to the project root directory"
        }
      }
    }
  }
}
```

### Output Schemas

Each tool has a corresponding output schema that may include:

- **Success Response** - Normal operation result
- **Error Response** - Error occurred during execution
- **Upgrade Required Response** - Premium feature requires license upgrade (with payment links)

## Tools

### Free Tools (Community Tier)

1. **`analyzeJakartaReadiness`** - Analyze project readiness
2. **`detectBlockers`** - Detect migration blockers
3. **`recommendVersions`** - Recommend Jakarta-compatible versions
4. **`check_env`** - Check environment variable status

### Premium Tools (Require PREMIUM License)

1. **`createMigrationPlan`** - Create comprehensive migration plan
2. **`analyzeMigrationImpact`** - Full migration impact analysis
3. **`verifyRuntime`** - Verify runtime execution of migrated application

## Upgrade Required Response

When a premium tool is called without the required license, it returns an upgrade response with payment links:

```json
{
  "status": "upgrade_required",
  "message": "The 'createMigrationPlan' tool requires a PREMIUM license...",
  "featureName": "One-click refactoring",
  "featureDescription": "Execute complete Jakarta migration refactoring...",
  "currentTier": "COMMUNITY",
  "requiredTier": "PREMIUM",
  "paymentLink": "https://buy.stripe.com/premium-link",
  "availablePlans": {
    "premium": "https://buy.stripe.com/premium-link",
    "enterprise": "https://buy.stripe.com/enterprise-link"
  },
  "upgradeMessage": "The 'One-click refactoring' feature requires a PREMIUM license..."
}
```

## Recent Updates

### 2026-01-XX: Licensing & Payment Links

- ✅ Enhanced `upgradeRequiredResponse` with payment link fields
- ✅ Added `featureName` and `featureDescription` to upgrade responses
- ✅ Added `availablePlans` object for all payment options
- ✅ Updated `analyzeMigrationImpact` to include upgrade response

See [SCHEMA_UPDATE_2026_LICENSING.md](SCHEMA_UPDATE_2026_LICENSING.md) for details.

## Validation

### Using JSON Schema Validators

**JavaScript (Ajv):**
```javascript
const Ajv = require('ajv');
const ajv = new Ajv();
const schema = require('./mcp-input-schemas.json');

const validate = ajv.compile(schema.toolInputs.analyzeJakartaReadiness);
const valid = validate({ projectPath: '/path/to/project' });
```

**Python (jsonschema):**
```python
import jsonschema
import json

with open('mcp-input-schemas.json') as f:
    schema = json.load(f)

jsonschema.validate(
    {"projectPath": "/path/to/project"},
    schema['toolInputs']['analyzeJakartaReadiness']
)
```

**Java (everit-org/json-schema):**
```java
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;

Schema schema = SchemaLoader.load(new JSONObject(schemaJson));
schema.validate(new JSONObject(inputJson));
```

## Type Generation

### TypeScript

```bash
npm install -g json-schema-to-typescript
json2ts -i mcp-input-schemas.json -o types.ts
```

### Java

Use tools like:
- [jsonschema2pojo](https://github.com/joelittlejohn/jsonschema2pojo)
- [quicktype](https://quicktype.io/)

## Integration Examples

### MCP Client Integration

```typescript
// TypeScript example
interface UpgradeRequiredResponse {
  status: 'upgrade_required';
  message: string;
  featureName: string;
  featureDescription: string;
  currentTier: 'COMMUNITY' | 'PREMIUM' | 'ENTERPRISE';
  requiredTier: 'PREMIUM' | 'ENTERPRISE';
  paymentLink?: string;
  availablePlans?: Record<string, string>;
  upgradeMessage: string;
}

function handleResponse(response: any) {
  if (response.status === 'upgrade_required') {
    const upgrade = response as UpgradeRequiredResponse;
    console.log(`Upgrade required: ${upgrade.featureName}`);
    if (upgrade.paymentLink) {
      console.log(`Upgrade at: ${upgrade.paymentLink}`);
    }
  }
}
```

## Documentation

- [Schema Overview](SCHEMA_OVERVIEW.md) - Complete schema documentation
- [Schema Update Summary](SCHEMA_UPDATE_SUMMARY.md) - Historical changes
- [Schema Update 2026 Licensing](SCHEMA_UPDATE_2026_LICENSING.md) - Latest licensing updates
- [Schema Comparison](SCHEMA_COMPARISON.md) - MCP vs Apify schemas
- [Apify Schemas](APIFY_SCHEMAS.md) - Apify-specific schema documentation
