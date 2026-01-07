# MCP Tool Licensing & Upgrade Information

## Overview

Premium MCP tools now display correct licensing and upgrade information when called without a valid license. This ensures users get helpful messages with payment links when they try to use premium features.

## Implementation

### Premium Tools

The following tools require a PREMIUM or ENTERPRISE license:

1. **`createMigrationPlan`** - Creates comprehensive migration plans
2. **`analyzeMigrationImpact`** - Full migration impact analysis
3. **`verifyRuntime`** - Runtime verification of migrated applications

### License Checking

Each premium tool now checks the user's license tier before executing:

```java
// Check if user has required tier (PREMIUM or ENTERPRISE)
if (!featureFlags.hasTier(FeatureFlagsProperties.LicenseTier.PREMIUM)) {
    return createUpgradeRequiredResponse(
        FeatureFlag.ONE_CLICK_REFACTOR,
        "The 'createMigrationPlan' tool requires a PREMIUM license..."
    );
}
```

### Upgrade Response Format

When a premium tool is called without the required license, it returns a structured JSON response:

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

## Features

### 1. License Tier Checking

- Tools check `featureFlags.hasTier(FeatureFlagsProperties.LicenseTier.PREMIUM)` before execution
- Returns upgrade message if tier is insufficient
- Allows execution if tier is sufficient

### 2. Payment Link Integration

- Integrates with `StripePaymentLinkService` to get actual payment links
- Maps license tiers to product names:
  - `PREMIUM` → `"premium"`
  - `ENTERPRISE` → `"enterprise"`
- Falls back gracefully if payment links are not configured

### 3. Upgrade Information

The `FeatureFlagsService` now provides:

- **`getUpgradeMessage(FeatureFlag)`** - Human-readable upgrade message
- **`getUpgradeInfo(FeatureFlag)`** - Structured upgrade information with payment links

### 4. Available Plans

The upgrade response includes all available payment plans, allowing users to:
- See all available tiers
- Choose the appropriate plan
- Access payment links directly

## Configuration

### Payment Links

Configure payment links in `application.yml`:

```yaml
jakarta:
  migration:
    stripe:
      payment-links:
        premium: https://buy.stripe.com/premium-link
        enterprise: https://buy.stripe.com/enterprise-link
```

### License Key

Set license key via environment variable:

```bash
export JAKARTA_MCP_LICENSE_KEY="your-license-key"
```

## Example Usage

### Community User Calls Premium Tool

**Request:**
```json
{
  "tool": "createMigrationPlan",
  "arguments": {
    "projectPath": "/path/to/project"
  }
}
```

**Response:**
```json
{
  "status": "upgrade_required",
  "message": "The 'createMigrationPlan' tool requires a PREMIUM license...",
  "currentTier": "COMMUNITY",
  "requiredTier": "PREMIUM",
  "paymentLink": "https://buy.stripe.com/premium-link",
  "availablePlans": {
    "premium": "https://buy.stripe.com/premium-link",
    "enterprise": "https://buy.stripe.com/enterprise-link"
  }
}
```

### Premium User Calls Premium Tool

**Request:**
```json
{
  "tool": "createMigrationPlan",
  "arguments": {
    "projectPath": "/path/to/project"
  }
}
```

**Response:**
```json
{
  "status": "success",
  "phaseCount": 5,
  "estimatedDuration": "120 minutes",
  "riskScore": 0.3,
  ...
}
```

## Benefits

1. **Clear Communication** - Users understand why a tool is unavailable
2. **Easy Upgrade Path** - Direct payment links in the response
3. **Better UX** - LLM can present upgrade options to users
4. **Flexible Configuration** - Payment links configurable per environment

## Integration with LLM Clients

LLM clients can now:

1. **Detect Upgrade Required** - Check `status === "upgrade_required"`
2. **Display Upgrade Message** - Show user-friendly message
3. **Present Payment Links** - Offer upgrade options with direct links
4. **Handle Gracefully** - Continue with available tools

### Example LLM Response

```
I see that the 'createMigrationPlan' tool requires a PREMIUM license, but you're currently on the COMMUNITY tier.

To unlock this feature, you can upgrade to PREMIUM at:
https://buy.stripe.com/premium-link

Available plans:
- Premium: https://buy.stripe.com/premium-link
- Enterprise: https://buy.stripe.com/enterprise-link

Would you like me to help you with the available community tools instead?
```

## Testing

To test the upgrade flow:

1. **Set COMMUNITY tier** (no license key or invalid key)
2. **Call premium tool** (e.g., `createMigrationPlan`)
3. **Verify upgrade response** with payment links
4. **Set PREMIUM tier** (valid premium license key)
5. **Call premium tool again** - should execute successfully

## Future Enhancements

- [ ] Add email-based license validation in upgrade messages
- [ ] Include trial period information
- [ ] Show feature comparison table
- [ ] Add credit balance information
- [ ] Support for promotional codes

## Related Documentation

- [Feature Flags System](../architecture/FEATURE_FLAGS.md)
- [Stripe Payment Links Implementation](./STRIPE_PAYMENT_LINKS_IMPLEMENTATION.md)
- [License API Implementation](./LICENSE_API_IMPLEMENTATION.md)

