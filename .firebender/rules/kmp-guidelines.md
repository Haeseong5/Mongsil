# Kotlin Multiplatform (KMP) Guidelines

## Module Structure

### Source Sets
- **commonMain**: Platform-agnostic business logic, shared models, and interfaces
- **androidMain**: Android-specific implementations (Activity, Fragment, Android APIs)
- **iosMain**: iOS-specific implementations (UIViewController, iOS APIs)

### Platform-Specific Code

#### Use expect/actual Pattern
```kotlin
// commonMain
expect class Platform() {
    val name: String
}

// androidMain
actual class Platform {
    actual val name: String = "Android ${android.os.Build.VERSION.SDK_INT}"
}

// iosMain
actual class Platform {
    actual val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}
```

## Best Practices

### 1. Maximize Code Sharing
- Put as much logic as possible in `commonMain`
- Only use platform-specific modules when absolutely necessary
- Consider using multiplatform libraries before platform-specific ones

### 2. Dependency Management
- Use version catalogs (`libs.versions.toml`) for dependency management
- Prefer KMP-compatible libraries (check for `-multiplatform` or `-common` artifacts)
- Common multiplatform libraries to consider:
  - Ktor for networking
  - SQLDelight for database
  - Koin for dependency injection
  - Kotlinx.serialization for JSON
  - Kotlinx.datetime for date/time handling

### 3. Resource Handling
- Use Compose Multiplatform resources for shared assets
- Keep platform-specific resources (Android res/, iOS Assets.xcassets) minimal

### 4. Testing
- Write tests in `commonTest` for shared logic
- Use platform-specific tests only for platform-specific implementations
- Aim for high test coverage in common code

## Common Pitfalls to Avoid

### ❌ Don't
- Don't put UI code in `commonMain` (unless using Compose Multiplatform)
- Don't use platform-specific APIs in common code without expect/actual
- Don't duplicate logic between platforms

### ✅ Do
- Use sealed classes for platform-agnostic result types
- Create platform-independent interfaces in common code
- Document why code is platform-specific when using expect/actual

## Example: Platform-Agnostic Result Handling

```kotlin
// commonMain
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

// Can be used across all platforms
suspend fun fetchUserData(): Result<User> {
    return try {
        val user = apiClient.getUser()
        Result.Success(user)
    } catch (e: Exception) {
        Result.Error(e)
    }
}
```
