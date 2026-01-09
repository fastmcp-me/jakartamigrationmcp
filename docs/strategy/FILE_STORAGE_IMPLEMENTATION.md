# File-Based License Storage Implementation

## Overview

Simple file-based storage for license sessions using JSON files. This replaces the SQLite implementation to keep things as simple as possible.

## Implementation

### Service: `LocalLicenseStorageService`

**Location**: `src/main/java/adrianmikula/jakartamigration/storage/service/LocalLicenseStorageService.java`

**Features**:
- Stores license sessions in a JSON file
- Thread-safe read/write operations using `ReentrantReadWriteLock`
- Automatic expiration handling
- Scheduled cleanup of expired sessions (hourly)
- Case-insensitive email handling

### Storage Format

**File**: `.mcp_license_sessions.json` (configurable)

**Format**: JSON object mapping email to session data
```json
{
  "user@example.com": {
    "email": "user@example.com",
    "licenseKey": "license_key_123",
    "tier": "PREMIUM",
    "createdAt": "2024-01-01T00:00:00Z",
    "expiresAt": "2024-01-02T00:00:00Z",
    "lastAccessedAt": "2024-01-01T12:00:00Z"
  }
}
```

### Configuration

**application.yml**:
```yaml
jakarta:
  migration:
    storage:
      file:
        enabled: ${FILE_STORAGE_ENABLED:false}
        path: ${FILE_STORAGE_PATH:.mcp_license_sessions.json}
        cache-ttl-hours: ${FILE_STORAGE_CACHE_TTL_HOURS:24}
```

**Environment Variables**:
- `FILE_STORAGE_ENABLED=true` - Enable file-based storage
- `FILE_STORAGE_PATH=.mcp_license_sessions.json` - Path to storage file
- `FILE_STORAGE_CACHE_TTL_HOURS=24` - TTL for cached sessions (hours)

### Integration

**LicenseService** automatically uses local storage when enabled:
1. Checks local storage first (fast, offline)
2. If not found, validates via Stripe API
3. Stores result in local storage for future use

### API Methods

- `getTierByEmail(String email)` - Get tier by email
- `getTierByLicenseKey(String licenseKey)` - Get tier by license key
- `storeSession(String email, String licenseKey, LicenseTier tier, Long ttlHours)` - Store session
- `deleteSession(String email)` - Delete session
- `cleanupExpiredSessions()` - Clean up expired sessions (scheduled)

### Thread Safety

Uses `ReentrantReadWriteLock` for thread-safe operations:
- Multiple concurrent reads allowed
- Exclusive write access during updates
- Prevents data corruption from concurrent access

### Cleanup

Expired sessions are automatically cleaned up:
- Runs hourly via `@Scheduled` annotation
- Removes sessions where `expiresAt < now`
- Logs number of sessions cleaned up

## Advantages

✅ **Simple** - No database dependencies
✅ **Lightweight** - Just a JSON file
✅ **Portable** - Easy to backup/restore
✅ **Fast** - In-memory reads after first load
✅ **Offline Support** - Works without internet after initial validation

## Limitations

⚠️ **Not for High Concurrency** - File-based storage has limits
⚠️ **Single Instance** - Not suitable for distributed systems
⚠️ **No Transactions** - Partial writes possible (mitigated by locks)

## Testing

**Test File**: `src/test/java/adrianmikula/jakartamigration/storage/service/LocalLicenseStorageServiceTest.java`

**Test Coverage**:
- Store and retrieve sessions
- Expiration handling
- Case-insensitive emails
- Session updates
- Deletion
- Null handling
- Default TTL

## Usage Example

```java
// Store session
localStorageService.storeSession(
    "user@example.com",
    "license_key_123",
    FeatureFlagsProperties.LicenseTier.PREMIUM,
    24L // 24 hours TTL
);

// Retrieve session
FeatureFlagsProperties.LicenseTier tier = 
    localStorageService.getTierByEmail("user@example.com");

// Delete session
localStorageService.deleteSession("user@example.com");
```

## Migration from SQLite

If you previously used SQLite:
1. Disable SQLite: `SQLITE_STORAGE_ENABLED=false`
2. Enable file storage: `FILE_STORAGE_ENABLED=true`
3. Sessions will be stored in JSON file instead of database

No data migration needed - just start using file storage.

