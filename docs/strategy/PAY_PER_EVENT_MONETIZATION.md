# Pay-Per-Event Monetization Strategy

## Overview

Instead of subscriptions, use a **pay-per-event** model where each MCP tool call is a billable event. This aligns better with MCP usage patterns and is simpler to implement.

## How It Works

1. **User purchases credits** (e.g., $10 for 100 credits)
2. **Each tool call costs credits** (e.g., 1 credit per call)
3. **Credits are consumed** on each tool execution
4. **When credits run out**, tool returns a message to purchase more
5. **No subscription required** - pay only for what you use

## Pricing Model

### Credit Packages

| Package | Credits | Price | Cost per Tool Call |
|---------|---------|-------|-------------------|
| **Starter** | 50 | $5 | $0.10 |
| **Professional** | 200 | $15 | $0.075 |
| **Enterprise** | 1000 | $50 | $0.05 |

### Tool Pricing (Credits per Call)

| Tool | Free Tier | Premium (Credits) |
|------|-----------|-------------------|
| `analyzeJakartaReadiness` | ✅ Free | 1 credit |
| `detectBlockers` | ✅ Free | 1 credit |
| `recommendVersions` | ✅ Free | 1 credit |
| `createMigrationPlan` | ❌ Premium | 2 credits |
| `analyzeMigrationImpact` | ❌ Premium | 3 credits |
| `verifyRuntime` | ❌ Premium | 2 credits |

**Note**: Free tier tools remain free to build user base. Premium tools require credits.

## Implementation Strategy

### Option 1: Simple Credit System (Recommended for MVP)

**How it works:**
1. User purchases credits via Stripe Payment Link
2. Stripe webhook delivers license key with credit balance
3. License key format: `credits_<balance>_<key>` (e.g., `credits_100_abc123`)
4. Each tool call decrements credit balance
5. Balance stored in-memory cache (or database for production)

**Pros:**
- ✅ Simple to implement
- ✅ No database required (can use in-memory cache)
- ✅ Works with Stripe Payment Links
- ✅ Fast to deploy

**Cons:**
- ❌ Balance lost on server restart (unless persisted)
- ❌ No real-time balance checking

### Option 2: API Key + External Credit Service

**How it works:**
1. User purchases API key via Stripe
2. API key is validated on each request
3. Credit balance checked via external service (or database)
4. Credits deducted after successful tool execution

**Pros:**
- ✅ Persistent credit storage
- ✅ Can check balance in real-time
- ✅ More scalable

**Cons:**
- ❌ Requires database or external service
- ❌ More complex to implement

### Option 3: Stripe Checkout Session (Per-Event)

**How it works:**
1. User calls premium tool
2. Tool returns Stripe Checkout Session link
3. User pays for single tool execution
4. After payment, tool executes

**Pros:**
- ✅ True pay-per-event
- ✅ No credit management needed

**Cons:**
- ❌ Friction on every premium tool call
- ❌ Not ideal UX

## Recommended: Option 1 (Simple Credit System)

### Implementation Steps

#### Step 1: Add Credit Management Tools

```java
@McpTool(name = "purchaseCredits", 
         description = "Get link to purchase credits for premium tools")
public String purchaseCredits() {
    return "Purchase Jakarta Migration MCP Credits:\n\n" +
           "Starter: 50 credits for $5 ($0.10/tool)\n" +
           "Professional: 200 credits for $15 ($0.075/tool)\n" +
           "Enterprise: 1000 credits for $50 ($0.05/tool)\n\n" +
           "Buy now: https://buy.stripe.com/credits-starter\n" +
           "After payment, you'll receive a license key with credits via email.";
}

@McpTool(name = "checkCredits", 
         description = "Check your remaining credit balance")
public String checkCredits(@McpToolParam(description = "Your license key") String licenseKey) {
    int balance = creditService.getBalance(licenseKey);
    if (balance < 0) {
        return "Invalid license key. Purchase credits: https://buy.stripe.com/credits-starter";
    }
    return "Remaining credits: " + balance + "\n" +
           "Purchase more: https://buy.stripe.com/credits-starter";
}
```

#### Step 2: Add Credit Service

```java
@Service
public class CreditService {
    private final Map<String, Integer> creditCache = new ConcurrentHashMap<>();
    
    public int getBalance(String licenseKey) {
        // Parse license key: credits_<balance>_<key>
        if (licenseKey.startsWith("credits_")) {
            String[] parts = licenseKey.split("_");
            if (parts.length >= 2) {
                try {
                    int balance = Integer.parseInt(parts[1]);
                    // Store in cache if not present
                    creditCache.putIfAbsent(licenseKey, balance);
                    return creditCache.get(licenseKey);
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        }
        return -1;
    }
    
    public boolean consumeCredits(String licenseKey, int amount) {
        int balance = getBalance(licenseKey);
        if (balance < amount) {
            return false;
        }
        creditCache.put(licenseKey, balance - amount);
        return true;
    }
}
```

#### Step 3: Update Premium Tools

```java
@McpTool(name = "createMigrationPlan", ...)
public String createMigrationPlan(...) {
    // Check credits
    String licenseKey = getLicenseKeyFromContext(); // How to get this?
    if (!creditService.consumeCredits(licenseKey, 2)) {
        return "Insufficient credits. You need 2 credits for this tool.\n" +
               "Current balance: " + creditService.getBalance(licenseKey) + "\n" +
               "Purchase credits: https://buy.stripe.com/credits-starter";
    }
    
    // Execute tool
    // ...
}
```

**Challenge**: How to get license key from MCP tool call context?

#### Step 4: License Key in Tool Parameters

Add optional `licenseKey` parameter to premium tools:

```java
@McpTool(name = "createMigrationPlan", ...)
public String createMigrationPlan(
    @McpToolParam(description = "Project path") String projectPath,
    @McpToolParam(description = "License key (optional, for premium features)") String licenseKey
) {
    if (licenseKey == null || licenseKey.isBlank()) {
        return "This tool requires credits. Purchase: https://buy.stripe.com/credits-starter";
    }
    
    if (!creditService.consumeCredits(licenseKey, 2)) {
        return "Insufficient credits. Purchase more: https://buy.stripe.com/credits-starter";
    }
    
    // Execute tool
    // ...
}
```

## Stripe Payment Link Setup

### Create Credit Packages

1. **Go to Stripe Dashboard** → Products
2. **Create 3 products:**
   - "Jakarta MCP Credits - Starter" ($5, one-time)
   - "Jakarta MCP Credits - Professional" ($15, one-time)
   - "Jakarta MCP Credits - Enterprise" ($50, one-time)

3. **Create Payment Links** for each
4. **Configure email delivery** with license key:
   - Format: `credits_<balance>_<random_key>`
   - Example: `credits_50_a1b2c3d4e5f6`

### Stripe Webhook (Optional, for Production)

For production, use Stripe webhooks to:
1. Generate license keys automatically
2. Store in database
3. Track usage

But for MVP, manual email delivery works fine.

## Credit Key Format

```
credits_<balance>_<random_key>
```

Examples:
- `credits_50_a1b2c3d4` - 50 credits
- `credits_200_x9y8z7w6` - 200 credits
- `credits_1000_m5n4o3p2` - 1000 credits

## Tool Execution Flow

1. User calls premium tool with license key
2. Tool checks credit balance
3. If sufficient:
   - Deduct credits
   - Execute tool
   - Return result
4. If insufficient:
   - Return error with purchase link
   - Don't execute tool

## Advantages of Pay-Per-Event

✅ **No subscription commitment** - users pay only for what they use  
✅ **Lower barrier to entry** - $5 starter package vs $29/month  
✅ **Fair pricing** - heavy users pay more, light users pay less  
✅ **Simple implementation** - no recurring billing complexity  
✅ **Better for MCP tools** - each tool call is naturally an "event"  

## Migration from Subscription Model

If you later want to add subscriptions:
- Keep pay-per-event as option
- Add subscription tier with unlimited tool calls
- Users can choose: credits (pay-per-use) or subscription (unlimited)

## Next Steps

1. ✅ Implement `CreditService`
2. ✅ Add `purchaseCredits` and `checkCredits` tools
3. ✅ Update premium tools to check credits
4. ✅ Create Stripe Payment Links for credit packages
5. ✅ Test credit consumption flow
6. ✅ Deploy to Railway

## Example User Flow

1. User calls `createMigrationPlan` without license key
2. Tool returns: "This tool requires 2 credits. Purchase: [link]"
3. User clicks link, pays $5 for 50 credits
4. User receives email: "Your license key: credits_50_a1b2c3d4"
5. User calls `createMigrationPlan` with license key
6. Tool executes, deducts 2 credits (balance: 48)
7. User can continue using premium tools until credits run out

