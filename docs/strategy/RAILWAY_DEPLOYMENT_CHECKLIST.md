# Railway Deployment Checklist

## ✅ What's Already Ready

### Railway Configuration
- ✅ `railway.json` - Build and start commands configured
- ✅ `application-mcp-streamable-http.yml` - Updated to use Railway's `PORT` variable
- ✅ Port handling: `PORT` (Railway) → `MCP_STREAMABLE_HTTP_PORT` → `8080` (default)

### Glama.ai Configuration
- ✅ `glama.json` - Updated with Railway URL structure, description, tags, pricing
- ✅ Ready for marketplace listing

### Existing Infrastructure
- ✅ `ApifyBillingService` - Already exists (for Apify platform)
- ✅ Feature flags system - Already implemented
- ✅ License validation - Already implemented (Apify + Stripe, but Stripe disabled)

## ⏭️ What Needs to Be Implemented

### 1. Credit-Based Pay-Per-Event System

**Status**: Not implemented yet

**Required Components**:

#### A. Credit Service
```java
@Service
public class CreditService {
    // Track credit balances (in-memory for MVP, database for production)
    // Parse license keys: credits_<balance>_<key>
    // Consume credits on tool execution
    // Check balance before tool execution
}
```

#### B. Credit Management Tools
- `purchaseCredits` - Returns Stripe Payment Link
- `checkCredits` - Shows remaining balance

#### C. Update Premium Tools
- Add optional `licenseKey` parameter
- Check credits before execution
- Consume credits after successful execution
- Return error with purchase link if insufficient credits

### 2. Stripe Payment Links Setup

**Status**: Needs to be created

**Required**:
1. Create 3 Stripe products:
   - Starter: 50 credits for $5
   - Professional: 200 credits for $15
   - Enterprise: 1000 credits for $50
2. Create Payment Links for each
3. Configure email delivery with license key format: `credits_<balance>_<random_key>`

### 3. Environment Variables for Railway

**Status**: Needs to be set in Railway dashboard

**Required**:
- `SPRING_PROFILES_ACTIVE=mcp-streamable-http` ✅ (Critical!)

**Optional** (for Stripe integration later):
- `STRIPE_VALIDATION_ENABLED=false` (keep disabled for now)
- `STRIPE_SECRET_KEY` (when ready to enable Stripe)

## Implementation Priority

### Phase 1: Basic Deployment (Do Now)
1. ✅ Railway auto-deploying (in progress)
2. ⏭️ Set `SPRING_PROFILES_ACTIVE=mcp-streamable-http` in Railway
3. ⏭️ Verify deployment works
4. ⏭️ Test MCP endpoint: `https://your-app.railway.app/mcp/streamable-http`
5. ⏭️ Update `glama.json` with actual Railway URL
6. ⏭️ List on Glama.ai

### Phase 2: Pay-Per-Event (Next)
1. ⏭️ Implement `CreditService`
2. ⏭️ Add `purchaseCredits` and `checkCredits` tools
3. ⏭️ Update premium tools to check/consume credits
4. ⏭️ Create Stripe Payment Links
5. ⏭️ Test credit flow

### Phase 3: Production (Later)
1. ⏭️ Add database for persistent credit storage
2. ⏭️ Add Stripe webhook for automatic license key generation
3. ⏭️ Add analytics/usage tracking
4. ⏭️ Add admin dashboard (optional)

## Current Status Summary

| Component | Status | Notes |
|-----------|--------|-------|
| Railway Config | ✅ Ready | `railway.json` configured |
| Port Handling | ✅ Ready | Uses Railway's `PORT` variable |
| Glama Config | ✅ Ready | `glama.json` updated |
| MCP Server | ✅ Ready | Streamable HTTP transport working |
| Credit System | ❌ Not Started | Needs implementation |
| Stripe Integration | ⚠️ Disabled | Can enable later |
| Monetization Tools | ❌ Not Started | Need to add |

## Next Steps

1. **Wait for Railway deployment** to complete
2. **Set environment variable**: `SPRING_PROFILES_ACTIVE=mcp-streamable-http`
3. **Test MCP endpoint** to verify it works
4. **Update `glama.json`** with actual Railway URL
5. **List on Glama.ai**
6. **Then implement pay-per-event** credit system

## Quick Test After Deployment

```bash
# Test MCP endpoint
curl -X POST https://your-app.railway.app/mcp/streamable-http \
  -H "Content-Type: application/json" \
  -d '{
    "jsonrpc": "2.0",
    "id": 1,
    "method": "tools/list",
    "params": {}
  }'
```

Expected: Should return all 6 Jakarta Migration tools + `check_env`

