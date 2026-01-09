# Railway License Server Backend

## Overview

Yes! **Railway is perfect** for hosting the license/credit server backend. This document outlines how to set it up.

## Architecture

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
│  │ (Local)   │  │
│  └─────┬─────┘  │
│        │        │
│        │ HTTP   │
│        │ API    │
└────────┼────────┘
         │
         │
    ┌────▼──────────────────┐
    │  License Server       │
    │  (Railway)            │
    │                       │
    │  - Validate keys      │
    │  - Track credits      │
    │  - Consume credits    │
    │  - Sync with Stripe   │
    └────┬──────────────────┘
         │
         │
    ┌────▼────┐
    │ Stripe  │
    │ API     │
    └─────────┘
```

## Why Railway is Perfect

### ✅ Advantages

1. **Simple Deployment**
   - Same platform as MCP server
   - Familiar workflow
   - Free tier available

2. **Cost-Effective**
   - Free tier: $5/month credit
   - Low traffic = low cost
   - Pay only for what you use

3. **Easy Integration**
   - Can use Spring Boot (same stack)
   - Or Node.js/Python (lighter weight)
   - REST API endpoints

4. **Scalable**
   - Auto-scales with traffic
   - No infrastructure management

5. **Database Support**
   - Railway provides PostgreSQL
   - Or use in-memory for MVP (upgrade later)

## Implementation Options

### Option 1: Spring Boot License Server (Recommended)

**Pros:**
- ✅ Same stack as MCP server
- ✅ Reuse existing code (LicenseService, StripeLicenseService)
- ✅ Easy to maintain

**Cons:**
- ⚠️ Slightly heavier (but Railway handles it)

**Structure:**
```
license-server/
├── src/main/java/
│   └── adrianmikula/license/
│       ├── LicenseController.java      # REST endpoints
│       ├── CreditService.java          # Credit management
│       ├── StripeService.java          # Stripe integration
│       └── LicenseValidationService.java
├── build.gradle.kts
└── application.yml
```

### Option 2: Node.js License Server (Lighter)

**Pros:**
- ✅ Very lightweight
- ✅ Fast startup
- ✅ Easy Stripe integration (Stripe Node.js SDK)

**Cons:**
- ⚠️ Different stack (but simple API)

**Structure:**
```
license-server/
├── src/
│   ├── server.js              # Express/Fastify server
│   ├── routes/
│   │   ├── license.js         # License validation
│   │   └── credits.js         # Credit management
│   └── services/
│       ├── stripe.js          # Stripe integration
│       └── database.js        # Credit storage
├── package.json
└── railway.json
```

## API Endpoints

### 1. Validate License Key

```http
GET /api/v1/licenses/{licenseKey}/validate
Authorization: Bearer {server-api-key}

Response:
{
  "valid": true,
  "tier": "PREMIUM",
  "expiresAt": "2026-02-07T00:00:00Z"
}
```

### 2. Get Credit Balance

```http
GET /api/v1/credits/{licenseKey}/balance
Authorization: Bearer {server-api-key}

Response:
{
  "balance": 48,
  "lastSync": "2026-01-07T10:30:00Z"
}
```

### 3. Consume Credits

```http
POST /api/v1/credits/{licenseKey}/consume
Authorization: Bearer {server-api-key}
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

### 4. Sync Credits (from Stripe)

```http
POST /api/v1/credits/{licenseKey}/sync
Authorization: Bearer {server-api-key}

Response:
{
  "balance": 50,
  "synced": true
}
```

## Database Schema (PostgreSQL)

### Credits Table

```sql
CREATE TABLE credits (
    license_key VARCHAR(255) PRIMARY KEY,
    balance INTEGER NOT NULL DEFAULT 0,
    last_sync TIMESTAMP NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_credits_last_sync ON credits(last_sync);
```

### Transactions Table

```sql
CREATE TABLE credit_transactions (
    id SERIAL PRIMARY KEY,
    license_key VARCHAR(255) NOT NULL,
    amount INTEGER NOT NULL,
    tool_name VARCHAR(100) NOT NULL,
    transaction_id VARCHAR(100) UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (license_key) REFERENCES credits(license_key)
);

CREATE INDEX idx_transactions_license_key ON credit_transactions(license_key);
CREATE INDEX idx_transactions_created_at ON credit_transactions(created_at);
```

## Implementation Steps

### Step 1: Create License Server Project

**Option A: New Spring Boot Project**
```bash
# Create new project
mkdir license-server
cd license-server
# Initialize Spring Boot project
```

**Option B: Separate Module in Existing Project**
```
JakartaMigrationMCP/
├── license-server/          # New module
│   ├── src/
│   └── build.gradle.kts
└── ...
```

### Step 2: Deploy to Railway

1. **Create new Railway project**: "jakarta-license-server"
2. **Connect GitHub repository**
3. **Set environment variables**:
   - `STRIPE_SECRET_KEY` - Your Stripe secret key
   - `SERVER_API_KEY` - API key for authenticating requests
   - `DATABASE_URL` - Railway provides this automatically

4. **Deploy**

### Step 3: Update MCP Server

Update `CreditService` in MCP server to call Railway API:

```java
@Service
public class CreditService {
    private final WebClient licenseApiClient;
    private final String licenseServerUrl = "https://jakarta-license-server.railway.app";
    
    public int getBalance(String licenseKey) {
        // Call Railway license server
        return licenseApiClient
            .get()
            .uri("{licenseServerUrl}/api/v1/credits/{licenseKey}/balance", 
                 licenseServerUrl, licenseKey)
            .header("Authorization", "Bearer " + serverApiKey)
            .retrieve()
            .bodyToMono(CreditBalanceResponse.class)
            .map(CreditBalanceResponse::getBalance)
            .block();
    }
    
    public boolean consumeCredits(String licenseKey, int amount) {
        // Call Railway license server
        return licenseApiClient
            .post()
            .uri("{licenseServerUrl}/api/v1/credits/{licenseKey}/consume", 
                 licenseServerUrl, licenseKey)
            .header("Authorization", "Bearer " + serverApiKey)
            .bodyValue(new ConsumeCreditsRequest(amount, "createMigrationPlan"))
            .retrieve()
            .bodyToMono(ConsumeCreditsResponse.class)
            .map(ConsumeCreditsResponse::isSuccess)
            .block();
    }
}
```

## Railway Configuration

### railway.json

```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "NIXPACKS",
    "buildCommand": "./gradlew bootJar"
  },
  "deploy": {
    "startCommand": "java -jar build/libs/license-server-*.jar",
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### Environment Variables

```bash
# Stripe
STRIPE_SECRET_KEY=sk_live_...
STRIPE_WEBHOOK_SECRET=whsec_...

# Server API Key (for authenticating MCP server requests)
SERVER_API_KEY=your-secret-api-key-here

# Database (Railway provides automatically)
DATABASE_URL=postgresql://...

# Application
SPRING_PROFILES_ACTIVE=production
SERVER_PORT=8080
```

## Cost Analysis

### Railway Costs

**License Server:**
- Free tier: $5/month credit
- Estimated usage: Very low (API calls only)
- **Estimated cost: $0-5/month**

**MCP Server (already deployed):**
- Free tier: $5/month credit
- **Total: $0-10/month for both**

### Stripe Costs

- Transaction fee: 2.9% + $0.30 per transaction
- No monthly fee
- **Cost: Only when users pay**

## Security

### API Authentication

**Option 1: Bearer Token (Simple)**
```http
Authorization: Bearer {server-api-key}
```

**Option 2: HMAC Signature (More Secure)**
```http
Authorization: HMAC {signature}
X-Timestamp: {timestamp}
```

### Rate Limiting

Use Railway's built-in rate limiting or add Spring Security:
```java
@Configuration
public class SecurityConfig {
    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.of("license-api", 
            RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .limitForPeriod(100)
                .build());
    }
}
```

## Deployment Checklist

- [ ] Create license server project
- [ ] Implement REST API endpoints
- [ ] Set up database schema
- [ ] Integrate Stripe API
- [ ] Deploy to Railway
- [ ] Set environment variables
- [ ] Test API endpoints
- [ ] Update MCP server `CreditService` to call Railway API
- [ ] Test end-to-end flow
- [ ] Monitor usage and costs

## Alternative: Use Existing MCP Server

**Simpler Option**: Add license/credit endpoints to existing MCP server:

```java
@RestController
@RequestMapping("/api/v1")
public class LicenseApiController {
    
    @GetMapping("/licenses/{licenseKey}/validate")
    public LicenseValidationResponse validateLicense(@PathVariable String licenseKey) {
        // Reuse existing LicenseService
    }
    
    @GetMapping("/credits/{licenseKey}/balance")
    public CreditBalanceResponse getBalance(@PathVariable String licenseKey) {
        // Return credit balance
    }
    
    @PostMapping("/credits/{licenseKey}/consume")
    public ConsumeCreditsResponse consumeCredits(
        @PathVariable String licenseKey,
        @RequestBody ConsumeCreditsRequest request
    ) {
        // Consume credits
    }
}
```

**Pros:**
- ✅ Single deployment
- ✅ Reuse existing code
- ✅ Simpler architecture

**Cons:**
- ⚠️ MCP server and license server share resources
- ⚠️ Less separation of concerns

## Recommendation

**Start Simple**: Add license/credit endpoints to existing MCP server on Railway.

**Scale Later**: If needed, split into separate service.

This gives you:
- ✅ Single Railway deployment
- ✅ Reuse existing Spring Boot code
- ✅ Lower complexity
- ✅ Easy to split later if needed

## Next Steps

1. Add REST endpoints to existing MCP server
2. Set up database (PostgreSQL on Railway)
3. Implement credit tracking
4. Test with local MCP server
5. Deploy to Railway

