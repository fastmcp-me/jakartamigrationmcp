# Apify License Validation Setup

This guide explains how to configure and use Apify for license validation in the Jakarta Migration MCP Server.

## Overview

Apify is a platform for hosting and monetizing MCP servers. The Jakarta Migration MCP Server can validate license keys (Apify API tokens) via Apify's API to determine user license tiers.

## How It Works

1. **User provides Apify API token** as their license key
2. **Server validates token** against Apify API (`/users/me` endpoint)
3. **Tier determined** based on user's Apify plan or actor access
4. **Results cached** to reduce API calls

## Configuration

### Basic Setup

Add to `application.yml`:

```yaml
jakarta:
  migration:
    apify:
      enabled: true
      api-url: https://api.apify.com/v2
      api-token: ${APIFY_API_TOKEN:}
      cache-ttl-seconds: 3600
      timeout-seconds: 5
      allow-offline-validation: true
```

### Environment Variables

```bash
# Your Apify API token (for making validation requests)
export APIFY_API_TOKEN="apify_api_your_token_here"

# Enable/disable Apify validation
export APIFY_VALIDATION_ENABLED="true"

# Apify API URL (usually doesn't need to change)
export APIFY_API_URL="https://api.apify.com/v2"

# Cache TTL in seconds
export APIFY_CACHE_TTL="3600"

# Request timeout in seconds
export APIFY_TIMEOUT="5"

# Actor ID (if your MCP is deployed as an Apify Actor)
export APIFY_ACTOR_ID="your-actor-id"

# Allow offline validation when API is unavailable
export APIFY_ALLOW_OFFLINE="true"
```

## Getting Your Apify API Token

1. **Sign up** at [Apify.com](https://apify.com)
2. **Go to Settings** → **Integrations** → **API tokens**
3. **Create a new token** or use your existing token
4. **Copy the token** (starts with `apify_api_`)

## User License Keys

Users provide their Apify API token as their license key:

```bash
# User sets their Apify API token
export JAKARTA_MCP_LICENSE_KEY="apify_api_user_token_here"
```

The server will:
1. Validate the token against Apify API
2. Determine tier based on user's plan:
   - **FREE/PERSONAL** → PREMIUM tier
   - **TEAM/ENTERPRISE** → ENTERPRISE tier
3. Cache the result for 1 hour (configurable)

## Tier Determination

Currently, the service determines tier based on:

- **User's Apify plan** (FREE, PERSONAL, TEAM, ENTERPRISE)
- **Actor access** (if `actor-id` is configured)

### Future Enhancements

You can enhance tier determination by:
- Checking custom metadata on user account
- Verifying actor access permissions
- Checking subscription status
- Validating custom license attributes

## Testing

### Test with Test Keys

For testing, you can use simple test keys:

```bash
# Premium test key
export JAKARTA_MCP_LICENSE_KEY="PREMIUM-test-key"

# Enterprise test key
export JAKARTA_MCP_LICENSE_KEY="ENTERPRISE-test-key"
```

These bypass Apify validation and use simple pattern matching.

### Test with Real Apify Token

1. Get a test Apify API token
2. Set it as license key:
   ```bash
   export JAKARTA_MCP_LICENSE_KEY="apify_api_your_test_token"
   ```
3. Run the server and check logs for validation results

### Disable Apify Validation

For local development without Apify:

```yaml
jakarta:
  migration:
    apify:
      enabled: false
```

This falls back to simple validation (PREMIUM-/ENTERPRISE- prefixes).

## Caching

License validation results are cached in memory to reduce API calls:

- **Default TTL:** 3600 seconds (1 hour)
- **Configurable:** via `cache-ttl-seconds`
- **Cache cleared:** on server restart or via `clearCache()` method

## Error Handling

The service handles various error scenarios:

1. **Invalid token (401/403):** Returns null (invalid license)
2. **Network errors:** Falls back to offline validation (if enabled)
3. **API unavailable:** Falls back to simple validation (if enabled)
4. **Timeout:** Falls back to offline validation

## Monitoring

Check logs for validation activity:

```
DEBUG: License validated and cached: apify_api_... -> PREMIUM
WARN: Apify license validation failed: Connection timeout
DEBUG: Falling back to simple validation
```

## Troubleshooting

### Validation Always Returns Null

- Check that `APIFY_API_TOKEN` is set (your token, not user's)
- Verify Apify API is accessible
- Check network connectivity
- Review logs for error messages

### Slow Validation

- Reduce `cache-ttl-seconds` if you need fresher results
- Check network latency to Apify API
- Consider increasing `timeout-seconds` if needed

### Offline Validation Not Working

- Ensure `allow-offline-validation: true`
- Check that test keys (PREMIUM-/ENTERPRISE-) work
- Verify fallback logic is enabled

## Next Steps

1. **Deploy MCP as Apify Actor** (optional)
   - Package your MCP server as an Apify Actor
   - Users can run it via Apify platform
   - Enable pay-per-execution billing

2. **Enhance Tier Determination**
   - Add custom metadata checks
   - Verify actor access permissions
   - Check subscription status

3. **Add Stripe Integration** (future)
   - Support Stripe subscription validation
   - Allow multiple license providers

## Related Documentation

- [Feature Flags Setup](FEATURE_FLAGS_SETUP.md)
- [Monetization Research](../research/monetisation.md)
- [Apify Documentation](https://docs.apify.com)

