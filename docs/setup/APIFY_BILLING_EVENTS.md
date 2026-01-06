# Apify Billing Events Configuration

This document lists all billable events that can be configured in your Apify dashboard for the Jakarta Migration MCP Server.

## Quick Reference

Use this list to quickly configure billable events in your Apify dashboard:

| Event Name | Price | Description |
|------------|-------|-------------|
| `migration-plan-created` | $0.05 | Creates comprehensive migration plan with phases and risk assessment |
| `runtime-verification-executed` | $0.10 | Verifies runtime execution of migrated Jakarta application |
| `one-click-refactor-executed` | $0.25 | Executes complete Jakarta migration refactoring automatically |
| `auto-fixes-applied` | $0.15 | Automatically fixes detected Jakarta migration issues |
| `binary-fixes-applied` | $0.20 | Fixes Jakarta migration issues in compiled binaries/JARs |
| `advanced-analysis-executed` | $0.10 | Deep dependency analysis with transitive conflict detection |

**Free Events** (no configuration needed):
- `analyzeJakartaReadiness` - Basic project analysis
- `detectBlockers` - Blocker detection
- `recommendVersions` - Version recommendations

**Apify Dashboard Configuration**:
1. Go to **Actor Settings** → **Monetization**
2. Enable **Pay-Per-Event (PPE)**
3. Click **"+ Add Event"** for each event above
4. Copy-paste event name, price, and description
5. Save configuration

## Overview

The Jakarta Migration MCP Server uses **Pay-Per-Event (PPE)** billing model on Apify. Each premium feature triggers a billable event when called by an agentic AI.

## Setup Guide

### Prerequisites

1. **Apify Account**: Sign up at https://apify.com
2. **Actor Created**: Deploy the MCP server as an Apify Actor
3. **Monetization Enabled**: Enable monetization in Actor settings

### Step 1: Enable Monetization

1. Go to your Apify Actor dashboard
2. Navigate to **Settings** → **Monetization**
3. Enable **Monetization**
4. Select **Pay-Per-Event (PPE)** pricing model

### Step 2: Configure Billable Events

Add each billable event from the list above:

1. Click **"+ Add Event"** in Apify dashboard
2. Enter event name (exact match required - see table above)
3. Set price (recommended prices shown in table)
4. Add description

### Step 3: Configure Environment Variables

Set these in your Apify Actor environment:

```bash
# Required for billing (auto-set by Apify)
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

### Environment Variables Reference

| Variable | Description | Required |
|----------|-------------|----------|
| `ACTOR_ID` | Apify Actor ID | Yes (auto-set) |
| `ACTOR_RUN_ID` | Apify Run ID | Yes (auto-set) |
| `ACTOR_MAX_TOTAL_CHARGE_USD` | Max spending limit | No |
| `APIFY_VALIDATION_ENABLED` | Enable Apify validation | No (default: true) |
| `APIFY_API_TOKEN` | Apify API token | Yes |

## Billable Events

### 1. `migration-plan-created`

**Trigger**: When `createMigrationPlan` tool is called successfully.

**Description**: Creates a comprehensive migration plan with phases, risk assessment, and estimated duration.

**Recommended Price**: $0.05 - $0.10 per event

**When Charged**: After successful plan creation

**Code Location**: `JakartaMigrationTools.createMigrationPlan()`

---

### 2. `runtime-verification-executed`

**Trigger**: When `verifyRuntime` tool is called successfully.

**Description**: Verifies runtime execution of a migrated Jakarta application, including bytecode analysis and execution testing.

**Recommended Price**: $0.10 - $0.20 per event

**When Charged**: After successful runtime verification

**Code Location**: `JakartaMigrationTools.verifyRuntime()`

---

### 3. `one-click-refactor-executed`

**Trigger**: When `executeOneClickRefactor` tool is called successfully.

**Description**: Executes complete Jakarta migration refactoring with a single command, applying all necessary changes automatically.

**Recommended Price**: $0.25 - $0.50 per event

**When Charged**: After successful refactoring execution

**Code Location**: `JakartaMigrationTools.executeOneClickRefactor()` (premium feature)

**Feature Flag**: `ONE_CLICK_REFACTOR` (PREMIUM tier required)

---

### 4. `auto-fixes-applied`

**Trigger**: When `autoFixIssues` tool is called successfully.

**Description**: Automatically fixes detected Jakarta migration issues without manual intervention.

**Recommended Price**: $0.15 - $0.30 per event

**When Charged**: After successful auto-fix execution

**Code Location**: `JakartaMigrationTools.autoFixIssues()` (premium feature)

**Feature Flag**: `AUTO_FIXES` (PREMIUM tier required)

---

### 5. `binary-fixes-applied`

**Trigger**: When binary fixes are applied to compiled JAR/WAR files.

**Description**: Fixes Jakarta migration issues in compiled binaries and JAR files using Apache Tomcat migration tool.

**Recommended Price**: $0.20 - $0.40 per event

**When Charged**: After successful binary fix

**Code Location**: `RefactoringEngine.refactorWithApacheTool()` (when used for binary fixes)

**Feature Flag**: `BINARY_FIXES` (PREMIUM tier required)

---

### 6. `advanced-analysis-executed`

**Trigger**: When advanced dependency analysis is performed.

**Description**: Deep dependency analysis with transitive conflict detection and resolution.

**Recommended Price**: $0.10 - $0.20 per event

**When Charged**: After successful advanced analysis

**Code Location**: `DependencyAnalysisModule.analyzeProject()` (when advanced mode enabled)

**Feature Flag**: `ADVANCED_ANALYSIS` (PREMIUM tier required)

---

## Free Events (No Charge)

The following tools are **free** and do not trigger billing events:

1. **`analyzeJakartaReadiness`** - Basic project analysis
2. **`detectBlockers`** - Blocker detection
3. **`recommendVersions`** - Version recommendations

These are community-tier features available to all users.

## Apify Dashboard Configuration

### Step 1: Navigate to Actor Settings

1. Go to your Apify Actor dashboard
2. Navigate to **Settings** → **Monetization**
3. Select **Pay-Per-Event (PPE)** pricing model

### Step 2: Configure Events

For each billable event, add a new event with:

- **Event Name**: Exact name from the list above (e.g., `migration-plan-created`)
- **Price**: Recommended price range (in USD)
- **Description**: Event description for users

### Example Configuration

| Event Name | Price | Description |
|------------|-------|-------------|
| `migration-plan-created` | $0.05 | Creates comprehensive migration plan |
| `runtime-verification-executed` | $0.10 | Verifies runtime execution of migrated app |
| `one-click-refactor-executed` | $0.25 | Executes complete Jakarta migration |
| `auto-fixes-applied` | $0.15 | Automatically fixes detected issues |
| `binary-fixes-applied` | $0.20 | Fixes issues in compiled binaries |
| `advanced-analysis-executed` | $0.10 | Deep dependency analysis |

### Step 3: Set Spending Limits

Configure default spending limits:

- **Default Max Charge**: $10.00 per run (recommended)
- **User Override**: Allow users to set custom limits via `ACTOR_MAX_TOTAL_CHARGE_USD`

## Code Integration

The billing service automatically charges for events when:

1. **Apify environment detected**: `ACTOR_ID` environment variable is set
2. **Apify validation enabled**: `jakarta.migration.apify.enabled=true`
3. **Premium feature executed**: Tool requires PREMIUM tier and is successfully executed

### Example Code

```java
@Service
@RequiredArgsConstructor
public class JakartaMigrationTools {
    
    private final ApifyBillingService apifyBillingService;
    
    @Tool(name = "createMigrationPlan")
    public String createMigrationPlan(String projectPath) {
        // ... create plan ...
        
        // Charge for event
        apifyBillingService.chargeEvent("migration-plan-created");
        
        return result;
    }
}
```

## Billing Service Behavior

### Automatic Detection

The `ApifyBillingService` automatically detects if billing should be enabled:

- ✅ **Enabled**: Running in Apify environment (`ACTOR_ID` set) and Apify validation enabled
- ❌ **Disabled**: Running locally or Apify validation disabled

### Spending Limits

The service respects spending limits:

- Checks `ACTOR_MAX_TOTAL_CHARGE_USD` environment variable
- Stops charging when limit is reached
- Logs warnings when limit is exceeded

### Error Handling

- Billing failures do **not** fail the operation
- Events are logged for debugging
- Charges are tracked per execution

## Testing Billing Events

### Local Testing

Billing is automatically disabled when running locally:

```bash
# Local execution (billing disabled)
java -jar app.jar
```

### Apify Testing

1. Deploy to Apify as Actor
2. Set `ACTOR_MAX_TOTAL_CHARGE_USD=1.00` for testing
3. Execute premium tools
4. Check Apify dashboard for charges

### Verification

Check billing logs:

```
INFO  - Charged for event: migration-plan-created (total charges: $0.05)
INFO  - Charged for event: runtime-verification-executed (total charges: $0.15)
```

## Pricing Recommendations

### Tier-Based Pricing

Consider different pricing for different license tiers:

- **Community**: Free (no charges)
- **Premium**: Standard pricing ($0.05 - $0.50 per event)
- **Enterprise**: Discounted pricing or volume-based

### Volume Discounts

Apify supports volume-based pricing:

- First 10 events: Full price
- Next 50 events: 10% discount
- 100+ events: 20% discount

## Monitoring

### Apify Dashboard

Monitor billing in Apify dashboard:

- **Revenue**: Total earnings per event
- **Usage**: Number of events per day/week/month
- **Top Events**: Most frequently charged events

### Application Logs

Billing events are logged:

```
INFO  - Charged for event: migration-plan-created (total charges: $0.05)
DEBUG - Event counts: {migration-plan-created=1, runtime-verification-executed=2}
```

## Troubleshooting

### Issue: Events not being charged

**Check**:
1. Is `ACTOR_ID` environment variable set?
2. Is `jakarta.migration.apify.enabled=true`?
3. Are events configured in Apify dashboard?
4. Check application logs for billing errors

### Issue: Charges exceeding limits

**Solution**: Set `ACTOR_MAX_TOTAL_CHARGE_USD` environment variable

### Issue: Wrong event names

**Solution**: Event names must match exactly (case-sensitive) with Apify dashboard configuration

## Code Integration Status

✅ **Billing Service**: `ApifyBillingService` created and configured  
✅ **Event Charging**: Integrated into premium tools  
✅ **Automatic Detection**: Billing enabled only in Apify environment  
✅ **Spending Limits**: Respects `ACTOR_MAX_TOTAL_CHARGE_USD`

### Tools with Billing Integration

| Tool | Event Name | Status |
|------|------------|--------|
| `createMigrationPlan` | `migration-plan-created` | ✅ Integrated |
| `verifyRuntime` | `runtime-verification-executed` | ✅ Integrated |
| `executeOneClickRefactor` | `one-click-refactor-executed` | ⚠️ Tool not yet implemented |
| `autoFixIssues` | `auto-fixes-applied` | ⚠️ Tool not yet implemented |
| Binary fixes | `binary-fixes-applied` | ⚠️ Needs integration |
| Advanced analysis | `advanced-analysis-executed` | ⚠️ Needs integration |

## How Billing Works

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

### Event Charging Flow

When a premium tool is executed:

1. Tool executes successfully
2. `apifyBillingService.chargeEvent("event-name")` is called
3. Service checks if billing is enabled
4. Event is logged and charged via Apify platform
5. Charge is tracked and limited by `ACTOR_MAX_TOTAL_CHARGE_USD`

### Example Code

```java
@Tool(name = "createMigrationPlan")
public String createMigrationPlan(String projectPath) {
    // ... create plan ...
    
    // Charge for event (only if billing enabled)
    apifyBillingService.chargeEvent("migration-plan-created");
    
    return result;
}
```

## Testing

### Local Testing

Billing is automatically disabled locally (no `ACTOR_ID` set):

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

For billing issues:
- Check Apify documentation: https://docs.apify.com/platform/actors/publishing/monetize/pay-per-event
- Review application logs for billing errors
- Verify event names match dashboard configuration

