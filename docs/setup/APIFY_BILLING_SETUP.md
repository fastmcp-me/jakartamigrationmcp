# Apify Billing Setup Guide

This guide explains how to set up and configure Apify billing for the Jakarta Migration MCP Server.

## Overview

The Jakarta Migration MCP Server supports **Pay-Per-Event (PPE)** billing on Apify. Premium features automatically trigger billable events when called by agentic AI assistants.

## Prerequisites

1. **Apify Account**: Sign up at https://apify.com
2. **Actor Created**: Deploy the MCP server as an Apify Actor
3. **Monetization Enabled**: Enable monetization in Actor settings

## Setup Steps

### Step 1: Enable Monetization

1. Go to your Apify Actor dashboard
2. Navigate to **Settings** → **Monetization**
3. Enable **Monetization**
4. Select **Pay-Per-Event (PPE)** pricing model

### Step 2: Configure Billable Events

Add each billable event from the [Billing Events List](APIFY_BILLING_EVENTS.md):

1. Click **"+ Add Event"**
2. Enter event name (exact match required):
   - `migration-plan-created`
   - `runtime-verification-executed`
   - `one-click-refactor-executed`
   - `auto-fixes-applied`
   - `binary-fixes-applied`
   - `advanced-analysis-executed`
3. Set price (recommended prices in documentation)
4. Add description

### Step 3: Configure Environment Variables

Set these in your Apify Actor environment:

```bash
# Required for billing
ACTOR_ID=your-actor-id
ACTOR_RUN_ID=auto-set-by-apify

# Optional: Set spending limit
ACTOR_MAX_TOTAL_CHARGE_USD=10.00

# Apify license validation
APIFY_VALIDATION_ENABLED=true
APIFY_API_TOKEN=your-apify-api-token
```

### Step 4: Verify Configuration

1. Deploy Actor with billing enabled
2. Test premium features
3. Check Apify dashboard for charges
4. Review application logs for billing events

## How It Works

### Automatic Billing Detection

The `ApifyBillingService` automatically detects if billing should be enabled:

```java
public boolean isBillingEnabled() {
    // Checks:
    // 1. Apify validation enabled
    // 2. ACTOR_ID environment variable set
    return apifyProperties.getEnabled() && 
           System.getenv("ACTOR_ID") != null;
}
```

### Event Charging

When a premium tool is executed:

1. Tool executes successfully
2. `apifyBillingService.chargeEvent("event-name")` is called
3. Service checks if billing is enabled
4. Event is logged and charged via Apify platform
5. Charge is tracked and limited by `ACTOR_MAX_TOTAL_CHARGE_USD`

### Example Flow

```java
@Tool(name = "createMigrationPlan")
public String createMigrationPlan(String projectPath) {
    // ... create plan ...
    
    // Charge for event (only if billing enabled)
    apifyBillingService.chargeEvent("migration-plan-created");
    
    return result;
}
```

## Configuration

### Application Configuration

In `application.yml`:

```yaml
jakarta:
  migration:
    apify:
      enabled: ${APIFY_VALIDATION_ENABLED:true}
      api-url: ${APIFY_API_URL:https://api.apify.com/v2}
      api-token: ${APIFY_API_TOKEN:}
```

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `ACTOR_ID` | Apify Actor ID | Yes (auto-set) |
| `ACTOR_RUN_ID` | Apify Run ID | Yes (auto-set) |
| `ACTOR_MAX_TOTAL_CHARGE_USD` | Max spending limit | No |
| `APIFY_VALIDATION_ENABLED` | Enable Apify validation | No (default: true) |
| `APIFY_API_TOKEN` | Apify API token | Yes |

## Testing

### Local Testing

Billing is automatically disabled locally:

```bash
# No ACTOR_ID set, billing disabled
java -jar app.jar
```

### Apify Testing

1. Deploy to Apify
2. Set `ACTOR_MAX_TOTAL_CHARGE_USD=1.00` for testing
3. Execute premium tools via MCP client
4. Check Apify dashboard → **Runs** → **Charges**

### Verification

Check logs for billing events:

```
INFO  - Charged for event: migration-plan-created (total charges: $0.05)
INFO  - Charged for event: runtime-verification-executed (total charges: $0.15)
```

## Pricing Strategy

### Recommended Prices

Based on feature complexity:

| Event | Price | Reason |
|-------|-------|--------|
| `migration-plan-created` | $0.05 | Low compute, frequent use |
| `runtime-verification-executed` | $0.10 | Medium compute, testing |
| `one-click-refactor-executed` | $0.25 | High value, complex operation |
| `auto-fixes-applied` | $0.15 | Medium value, automation |
| `binary-fixes-applied` | $0.20 | Medium-high compute |
| `advanced-analysis-executed` | $0.10 | Medium compute, analysis |

### Tier-Based Pricing

Consider different pricing for different tiers:

- **Community**: Free (no charges)
- **Premium**: Standard pricing
- **Enterprise**: Volume discounts

## Monitoring

### Apify Dashboard

Monitor in Apify dashboard:

- **Revenue**: Total earnings
- **Usage**: Events per period
- **Top Events**: Most charged events
- **User Spending**: Per-user charges

### Application Logs

Billing events are logged:

```
INFO  - Charged for event: migration-plan-created (total charges: $0.05)
DEBUG - Event counts: {migration-plan-created=1}
```

### Metrics

Track key metrics:

- Total charges per run
- Event frequency
- Average charge per user
- Revenue trends

## Troubleshooting

### Events Not Being Charged

**Check**:
1. Is `ACTOR_ID` set? (should be auto-set by Apify)
2. Is `jakarta.migration.apify.enabled=true`?
3. Are events configured in Apify dashboard?
4. Check application logs for errors

**Solution**: Verify environment variables and Apify Actor configuration

### Charges Exceeding Limits

**Check**: `ACTOR_MAX_TOTAL_CHARGE_USD` value

**Solution**: Set appropriate limit or increase limit

### Wrong Event Names

**Check**: Event names must match exactly (case-sensitive)

**Solution**: Verify event names in code match Apify dashboard

### Billing Disabled Unexpectedly

**Check**:
1. `APIFY_VALIDATION_ENABLED` setting
2. `ACTOR_ID` environment variable
3. Application logs

**Solution**: Ensure Apify environment is properly configured

## Best Practices

1. **Set Spending Limits**: Always set `ACTOR_MAX_TOTAL_CHARGE_USD` to protect users
2. **Clear Pricing**: Document prices in Actor description
3. **Monitor Usage**: Track event frequency and adjust pricing
4. **Error Handling**: Billing failures should not break functionality
5. **Logging**: Log all billing events for debugging

## Support

- **Apify Docs**: https://docs.apify.com/platform/actors/publishing/monetize/pay-per-event
- **Billing Events**: See [APIFY_BILLING_EVENTS.md](APIFY_BILLING_EVENTS.md)
- **Application Logs**: Check logs for billing errors

