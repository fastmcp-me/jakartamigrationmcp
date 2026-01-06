# Apify Billing Events - Quick Reference

Use this list to configure billable events in your Apify dashboard.

## Billable Events (Copy-Paste for Apify Dashboard)

### Event 1: Migration Plan Creation
- **Event Name**: `migration-plan-created`
- **Price**: $0.05
- **Description**: Creates comprehensive migration plan with phases and risk assessment

### Event 2: Runtime Verification
- **Event Name**: `runtime-verification-executed`
- **Price**: $0.10
- **Description**: Verifies runtime execution of migrated Jakarta application

### Event 3: One-Click Refactor
- **Event Name**: `one-click-refactor-executed`
- **Price**: $0.25
- **Description**: Executes complete Jakarta migration refactoring automatically

### Event 4: Auto-Fixes
- **Event Name**: `auto-fixes-applied`
- **Price**: $0.15
- **Description**: Automatically fixes detected Jakarta migration issues

### Event 5: Binary Fixes
- **Event Name**: `binary-fixes-applied`
- **Price**: $0.20
- **Description**: Fixes Jakarta migration issues in compiled binaries/JARs

### Event 6: Advanced Analysis
- **Event Name**: `advanced-analysis-executed`
- **Price**: $0.10
- **Description**: Deep dependency analysis with transitive conflict detection

## Free Events (No Configuration Needed)

These tools are free and do not trigger billing:

- `analyzeJakartaReadiness` - Basic project analysis
- `detectBlockers` - Blocker detection  
- `recommendVersions` - Version recommendations

## Apify Dashboard Configuration

1. Go to **Actor Settings** → **Monetization**
2. Enable **Pay-Per-Event (PPE)**
3. Click **"+ Add Event"** for each event above
4. Copy-paste event name, price, and description
5. Save configuration

## Code Integration Status

✅ **Billing Service**: `ApifyBillingService` created and configured  
✅ **Event Charging**: Integrated into premium tools  
✅ **Automatic Detection**: Billing enabled only in Apify environment  
✅ **Spending Limits**: Respects `ACTOR_MAX_TOTAL_CHARGE_USD`  

## Tools with Billing Integration

| Tool | Event Name | Status |
|------|------------|--------|
| `createMigrationPlan` | `migration-plan-created` | ✅ Integrated |
| `verifyRuntime` | `runtime-verification-executed` | ✅ Integrated |
| `executeOneClickRefactor` | `one-click-refactor-executed` | ⚠️ Tool not yet implemented |
| `autoFixIssues` | `auto-fixes-applied` | ⚠️ Tool not yet implemented |
| Binary fixes | `binary-fixes-applied` | ⚠️ Needs integration |
| Advanced analysis | `advanced-analysis-executed` | ⚠️ Needs integration |

## Next Steps

1. **Configure Events**: Add all events to Apify dashboard
2. **Deploy Actor**: Deploy MCP server to Apify
3. **Test Billing**: Execute premium tools and verify charges
4. **Monitor**: Track revenue and usage in Apify dashboard

For detailed setup instructions, see [APIFY_BILLING_SETUP.md](APIFY_BILLING_SETUP.md).

