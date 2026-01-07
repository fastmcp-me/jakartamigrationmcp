# Local STDIO Monetization Strategy

## Answer: YES, Local STDIO Works with Monetization ✅

The local stdio option **fully supports** pay-per-event monetization. Here's how:

## How It Works

### 1. License Key Validation (Already Implemented)

**Current System:**
- License key is passed via environment variable: `JAKARTA_MCP_LICENSE_KEY`
- Validation happens via **API calls** to Stripe/Apify (works from local machine)
- Results are **cached** to reduce API calls
- Works offline with `allow-offline-validation: true`

**Example:**
```bash
# User sets license key
export JAKARTA_MCP_LICENSE_KEY="credits_50_a1b2c3d4"

# Run MCP server (stdio)
jakarta-migration-mcp
```

**What Happens:**
1. MCP server reads `JAKARTA_MCP_LICENSE_KEY` from environment
2. `FeatureFlagsService.getCurrentTier()` validates key via `LicenseService`
3. `LicenseService` calls Stripe/Apify API to validate (if online)
4. Tier is cached for 1 hour (reduces API calls)
5. Tools check tier before executing premium features

### 2. Credit System (To Be Implemented)

**How It Will Work:**

```java
@Service
public class CreditService {
    private final WebClient creditApiClient; // Calls your credit server
    
    public boolean consumeCredits(String licenseKey, int amount) {
        // 1. Check local cache first
        int cachedBalance = getCachedBalance(licenseKey);
        if (cachedBalance < amount) {
            // 2. Sync with remote server
            int remoteBalance = syncWithServer(licenseKey);
            if (remoteBalance < amount) {
                return false; // Insufficient credits
            }
        }
        
        // 3. Consume credits locally
        decrementLocalBalance(licenseKey, amount);
        
        // 4. Sync with server (async, non-blocking)
        syncConsumptionAsync(licenseKey, amount);
        
        return true;
    }
}
```

**Key Points:**
- ✅ **Local execution** - Tool runs on user's machine
- ✅ **Remote validation** - Credits checked/synced via API
- ✅ **Caching** - Reduces API calls, improves performance
- ✅ **Offline support** - Can work offline with cached balance
- ✅ **Security** - License key validated server-side

### 3. User Flow

**Step 1: User Purchases Credits**
```
User → Stripe Payment Link → Pays $5 → Receives license key: credits_50_a1b2c3d4
```

**Step 2: User Configures MCP**
```json
{
  "mcpServers": {
    "jakarta-migration": {
      "command": "jakarta-migration-mcp",
      "env": {
        "JAKARTA_MCP_LICENSE_KEY": "credits_50_a1b2c3d4"
      }
    }
  }
}
```

**Step 3: User Calls Premium Tool**
```
AI calls: createMigrationPlan(projectPath="...", licenseKey="credits_50_a1b2c3d4")
```

**Step 4: Tool Validates & Consumes Credits**
```java
@McpTool(name = "createMigrationPlan")
public String createMigrationPlan(
    @McpToolParam String projectPath,
    @McpToolParam String licenseKey
) {
    // 1. Validate license key (API call to Stripe/your server)
    if (!creditService.isValidLicense(licenseKey)) {
        return "Invalid license key. Purchase: https://buy.stripe.com/...";
    }
    
    // 2. Check credits (API call, cached)
    if (!creditService.hasCredits(licenseKey, 2)) {
        return "Insufficient credits. Purchase: https://buy.stripe.com/...";
    }
    
    // 3. Execute tool
    MigrationPlan plan = migrationPlanner.createPlan(projectPath);
    
    // 4. Consume credits (syncs with server)
    creditService.consumeCredits(licenseKey, 2);
    
    return plan.toJson();
}
```

## Architecture

### Local STDIO + Remote Validation

```
┌─────────────────┐
│  User's Machine │
│                 │
│  ┌───────────┐  │
│  │   Cursor  │  │
│  │   (MCP    │  │
│  │   Client) │  │
│  └─────┬─────┘  │
│        │        │
│        │ STDIO  │
│        │        │
│  ┌─────▼─────┐  │
│  │ jakarta-  │  │
│  │ migration │  │
│  │ -mcp      │  │
│  │ (JAR)     │  │
│  └─────┬─────┘  │
│        │        │
│        │ HTTP   │
│        │ API    │
└────────┼────────┘
         │
         │
    ┌────▼────┐
    │ Credit  │
    │ Server  │
    │ (Railway│
    │ /Stripe)│
    └─────────┘
```

**Key Points:**
- ✅ Tool execution: **Local** (fast, private)
- ✅ License validation: **Remote** (secure, controlled)
- ✅ Credit tracking: **Remote** (can't be bypassed)
- ✅ Caching: **Local** (performance, offline support)

## Security Considerations

### Can Users Bypass Payment?

**No, because:**

1. **License Key Validation**
   - Keys are validated server-side (Stripe/Apify API)
   - Keys are cryptographically signed
   - Can't be forged without server access

2. **Credit Balance**
   - Stored server-side (single source of truth)
   - Local cache is just for performance
   - Server syncs on every credit check

3. **Offline Mode**
   - Limited to cached balance
   - Must sync online periodically
   - Server enforces limits

### Example Attack Scenarios

**Scenario 1: User tries to modify JAR**
- ❌ **Mitigation**: License validation happens via API call
- ❌ **Mitigation**: Credit balance checked server-side
- ✅ **Result**: Can't bypass without valid license key

**Scenario 2: User tries to fake license key**
- ❌ **Mitigation**: Keys are validated against Stripe/Apify
- ❌ **Mitigation**: Keys are cryptographically signed
- ✅ **Result**: Invalid keys rejected by server

**Scenario 3: User tries to use offline forever**
- ⚠️ **Limitation**: Can use cached balance
- ✅ **Mitigation**: Cache expires (1 hour)
- ✅ **Mitigation**: Must sync online to refresh
- ✅ **Result**: Limited offline usage, must pay for more

## Implementation Details

### Credit Service API

**Endpoint**: `https://your-credit-server.com/api/credits`

**Check Balance:**
```http
GET /api/credits/{licenseKey}
Authorization: Bearer {your-api-key}

Response:
{
  "balance": 48,
  "lastSync": "2026-01-07T10:30:00Z"
}
```

**Consume Credits:**
```http
POST /api/credits/{licenseKey}/consume
Authorization: Bearer {your-api-key}
Content-Type: application/json

{
  "amount": 2,
  "tool": "createMigrationPlan",
  "timestamp": "2026-01-07T10:31:00Z"
}

Response:
{
  "success": true,
  "newBalance": 46,
  "transactionId": "txn_abc123"
}
```

### Caching Strategy

```java
@Service
public class CreditService {
    private final Map<String, CachedBalance> cache = new ConcurrentHashMap<>();
    
    private static class CachedBalance {
        int balance;
        LocalDateTime expiresAt;
        LocalDateTime lastSync;
    }
    
    public int getBalance(String licenseKey) {
        CachedBalance cached = cache.get(licenseKey);
        
        // Use cache if valid (less than 5 minutes old)
        if (cached != null && cached.expiresAt.isAfter(LocalDateTime.now())) {
            return cached.balance;
        }
        
        // Sync with server
        int remoteBalance = syncWithServer(licenseKey);
        
        // Update cache
        cache.put(licenseKey, new CachedBalance(
            remoteBalance,
            LocalDateTime.now().plusMinutes(5), // Cache for 5 minutes
            LocalDateTime.now()
        ));
        
        return remoteBalance;
    }
}
```

## Benefits of Local STDIO + Remote Validation

### ✅ Performance
- Tool execution: **Local** (no network latency)
- License check: **Cached** (API call only when needed)
- Credit check: **Cached** (syncs periodically)

### ✅ Privacy
- Source code: **Never leaves local machine**
- Analysis: **Happens locally**
- Only metadata sent: License key, credit consumption

### ✅ Security
- License validation: **Server-side** (can't be bypassed)
- Credit tracking: **Server-side** (single source of truth)
- Offline support: **Limited** (must sync periodically)

### ✅ User Experience
- Fast execution (local)
- Works offline (with cached balance)
- Simple setup (npm install)
- No file uploads needed

## Comparison: Cloud vs Local STDIO

| Aspect | Cloud (Railway) | Local STDIO |
|--------|----------------|-------------|
| **File Access** | ❌ Can't access local files | ✅ Full filesystem access |
| **Performance** | ⚠️ Network latency | ✅ Instant (local) |
| **Privacy** | ⚠️ Code uploaded | ✅ Code stays local |
| **Monetization** | ✅ Server-side validation | ✅ Remote API validation |
| **Offline** | ❌ Requires internet | ✅ Works with cache |
| **Setup** | ✅ Just URL | ⚠️ npm install |

## Conclusion

**Local STDIO + Remote Validation = Best of Both Worlds**

- ✅ **Local execution** for performance and privacy
- ✅ **Remote validation** for security and monetization
- ✅ **Caching** for offline support and performance
- ✅ **Simple setup** via npm package

**This is the recommended approach for production use.**

