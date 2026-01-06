# Apify Actor Schemas

This document describes the Apify-specific schemas for the Jakarta Migration MCP Server when deployed as an Apify Actor.

## Overview

When deployed to Apify, the MCP server uses Apify's Actor schema format, which is different from standard JSON Schema. Apify schemas are used to:

1. **Input Schema** - Auto-generate UI forms in Apify Console
2. **Output Schema** - Define where output is stored and how to access it

## Files

- **`.actor/input_schema.json`** - Apify input schema for Actor UI generation
- **`.actor/output_schema.json`** - Apify output schema for output display
- **`.actor/actor.json`** - Actor metadata and configuration

## Input Schema

The input schema (`input_schema.json`) follows [Apify's input schema specification](https://docs.apify.com/platform/actors/development/actor-definition/input-schema) and defines:

### Configuration Sections

1. **MCP Configuration**
   - `mcpTransport` - Transport mode (stdio/sse)
   - `mcpSsePort` - SSE port number
   - `mcpSsePath` - SSE endpoint path

2. **License Configuration**
   - `licenseKey` - User's license key (Apify API token or Stripe key)
   - `apifyValidationEnabled` - Enable/disable Apify validation
   - `stripeValidationEnabled` - Enable/disable Stripe validation
   - `defaultTier` - Default license tier

3. **Server Configuration**
   - `apifyApiToken` - Server's Apify API token (for validation)
   - `stripeSecretKey` - Server's Stripe secret key (for validation)

4. **Billing Configuration**
   - `maxTotalChargeUsd` - Maximum charge per run

### Schema Features

- **Auto-generated UI** - Apify Console automatically generates input forms
- **Secret Input** - `apifyApiToken` and `stripeSecretKey` use `"editor": "secret"` for secure input
- **Section Captions** - Groups related fields together
- **Enum Titles** - Human-readable labels for enum values
- **Defaults** - Pre-filled values for convenience

## Output Schema

The output schema (`output_schema.json`) follows [Apify's output schema specification](https://docs.apify.com/platform/actors/development/actor-definition/output-schema) and defines:

### Output Properties

1. **MCP Endpoint** - SSE endpoint for MCP protocol
2. **Health Check** - Application health endpoint
3. **Metrics** - Application metrics endpoint
4. **Info** - Application information endpoint

### Template Variables

The output schema uses Apify template variables:

- `{{run.containerUrl}}` - URL of the web server running inside the run
- `{{links.apiDefaultDatasetUrl}}` - API URL of default dataset
- `{{links.apiDefaultKeyValueStoreUrl}}` - API URL of default key-value store

### Output Display

When the Actor run finishes, Apify Console displays:

- **MCP Server Endpoint** - Link to SSE endpoint for MCP clients
- **Health Check** - Link to health check endpoint
- **Metrics** - Link to metrics endpoint
- **Info** - Link to application info endpoint

## Actor Configuration

The `actor.json` file defines:

- **Actor metadata** - Name, title, version, description
- **Schema references** - Paths to input and output schemas
- **Storage configuration** - Dataset and key-value store access

## Differences from MCP Schemas

### Input Schemas

| Aspect | MCP Schema | Apify Schema |
|--------|-----------|--------------|
| **Purpose** | Validate MCP tool inputs | Generate Apify UI forms |
| **Format** | JSON Schema Draft 7 | Apify-specific format |
| **Structure** | Tool-specific schemas | Single unified schema |
| **Fields** | Tool parameters | Actor configuration |

### Output Schemas

| Aspect | MCP Schema | Apify Schema |
|--------|-----------|--------------|
| **Purpose** | Validate MCP tool outputs | Define output storage locations |
| **Format** | JSON Schema Draft 7 | Apify-specific format |
| **Content** | Data structure definitions | URL templates for accessing output |
| **Focus** | Response validation | Output display in Console |

## Usage

### For Apify Deployment

1. **Deploy Actor** with `.actor/` folder containing schemas
2. **Apify Console** auto-generates input UI from `input_schema.json`
3. **Users configure** Actor via the generated UI
4. **Output tab** displays links defined in `output_schema.json`

### For MCP Client Integration

Use the standard JSON Schema files:
- `docs/schemas/mcp-input-schemas.json` - For MCP tool input validation
- `docs/schemas/mcp-output-schemas.json` - For MCP tool output validation

## References

- [Apify Input Schema Documentation](https://docs.apify.com/platform/actors/development/actor-definition/input-schema)
- [Apify Output Schema Documentation](https://docs.apify.com/platform/actors/development/actor-definition/output-schema)
- [Apify Actor Specification](https://docs.apify.com/platform/actors/development/actor-definition/)

