# Schema Update - Licensing & Payment Links

## Date: 2026-01-XX

## Overview

Updated schemas to reflect the new licensing and payment link integration in upgrade responses.

## Changes Made

### 1. Enhanced `upgradeRequiredResponse` Schema

**Location**: `docs/schemas/mcp-output-schemas.json`

#### New Required Fields

- **`featureName`** (string)
  - Name of the feature that requires upgrade
  - Example: "One-click refactoring"

- **`featureDescription`** (string)
  - Description of the feature that requires upgrade
  - Example: "Execute complete Jakarta migration refactoring with a single command"

#### New Optional Fields

- **`paymentLink`** (string, URI, optional)
  - Direct payment link for the required tier
  - Only present if payment links are configured
  - Example: "https://buy.stripe.com/premium-link"

- **`availablePlans`** (object, optional)
  - Map of all available payment plans
  - Keys are plan names (e.g., "premium", "enterprise")
  - Values are payment link URLs
  - Only present if payment links are configured
  - Example:
    ```json
    {
      "premium": "https://buy.stripe.com/premium-link",
      "enterprise": "https://buy.stripe.com/enterprise-link"
    }
    ```

#### Updated Fields

- **`message`** - Now provides more detailed explanation
- **`upgradeMessage`** - Enhanced with payment link information

### 2. Updated Tool Output Mappings

**`analyzeMigrationImpact`** now includes `upgradeRequiredResponse` as a possible output:

```json
{
  "analyzeMigrationImpact": {
    "oneOf": [
      { "$ref": "#/definitions/migrationImpactResponse" },
      { "$ref": "#/definitions/errorResponse" },
      { "$ref": "#/definitions/upgradeRequiredResponse" }
    ]
  }
}
```

### 3. Updated Example Responses

**Location**: `docs/schemas/example-requests-responses.json`

Added comprehensive examples:
- `upgradeRequiredResponse` - Full example with all new fields
- `analyzeMigrationImpact` - Success response example
- `analyzeMigrationImpactUpgradeRequired` - Upgrade required example

## Complete Upgrade Response Schema

```json
{
  "status": "upgrade_required",
  "message": "The 'createMigrationPlan' tool requires a PREMIUM license...",
  "featureName": "One-click refactoring",
  "featureDescription": "Execute complete Jakarta migration refactoring...",
  "requiredTier": "PREMIUM",
  "currentTier": "COMMUNITY",
  "paymentLink": "https://buy.stripe.com/premium-link",
  "availablePlans": {
    "premium": "https://buy.stripe.com/premium-link",
    "enterprise": "https://buy.stripe.com/enterprise-link"
  },
  "upgradeMessage": "The 'One-click refactoring' feature requires a PREMIUM license..."
}
```

## Tools That Return Upgrade Responses

1. **`createMigrationPlan`** - Requires PREMIUM tier
2. **`analyzeMigrationImpact`** - Requires PREMIUM tier (NEW)
3. **`verifyRuntime`** - Requires PREMIUM tier

## Benefits

1. **Better UX** - LLM clients can present upgrade options with direct payment links
2. **Clear Information** - Users see exactly what feature requires upgrade
3. **Flexible Configuration** - Payment links optional, graceful fallback
4. **Complete Context** - All available plans shown for comparison

## Validation

✅ **Schema Valid**: All JSON schemas validate correctly
✅ **Examples Updated**: Example responses reflect new structure
✅ **Backward Compatible**: Existing fields remain, new fields are optional where appropriate

## Related Documentation

- [MCP Tool Licensing](../strategy/MCP_TOOL_LICENSING.md)
- [Stripe Payment Links Implementation](../strategy/STRIPE_PAYMENT_LINKS_IMPLEMENTATION.md)
- [Schema Overview](./SCHEMA_OVERVIEW.md)

