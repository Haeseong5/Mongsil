# Dependency Management Guidelines

## Version Catalog

This project uses Gradle Version Catalogs (`gradle/libs.versions.toml`) for centralized dependency management.

### Adding New Dependencies

1. **Check if the library supports KMP**
   - Look for `-multiplatform` artifact
   - Check official documentation for KMP support
   - Verify on Maven Central or library's GitHub

2. **Add to version catalog**
   ```toml
   [versions]
   library-name = "x.y.z"
   
   [libraries]
   library-name = { module = "com.example:library", version.ref = "library-name" }
   ```

3. **Add to appropriate module**
   ```kotlin
   // For common code
   commonMain.dependencies {
       implementation(libs.library.name)
   }
   
   // For platform-specific
   androidMain.dependencies {
       implementation(libs.library.name)
   }
   ```

## Recommended Libraries

### Networking
- **Ktor Client**: Multiplatform HTTP client
  - Use for all network operations in common code
  - Configure platform-specific engines (Android, iOS)

### Database
- **SQLDelight**: Type-safe SQL for KMP
  - Define schema in common code
  - Platform-specific drivers for Android and iOS

### Serialization
- **Kotlinx.serialization**: Official Kotlin serialization
  - Prefer over Gson/Moshi for KMP projects
  - Built-in multiplatform support

### Dependency Injection
- **Koin**: Lightweight DI for KMP
  - Simple setup for multiplatform projects
  - Good Compose integration

### Coroutines
- **Kotlinx.coroutines**: Already included
  - Use for all async operations
  - Available on all platforms

### UI (if using Compose Multiplatform)
- **Compose Multiplatform**: Share UI code
- **Voyager/Decompose**: Navigation libraries

## Dependency Guidelines

### Version Management
- Keep dependencies up to date (review quarterly)
- Test updates in a separate branch
- Use stable versions for production
- Document breaking changes in migration notes

### Scope Guidelines
- Use `implementation` for internal dependencies
- Use `api` only when types are exposed in public API
- Use `compileOnly` for provided dependencies

### Platform-Specific Dependencies
- Minimize platform-specific dependencies
- Document why platform-specific library is needed
- Consider wrapping in expect/actual if used in common code

## Examples

### Good: Using KMP Library
```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation(libs.ktor.client.core)
    implementation(libs.kotlinx.serialization.json)
}

androidMain.dependencies {
    implementation(libs.ktor.client.android)
}

iosMain.dependencies {
    implementation(libs.ktor.client.darwin)
}
```

### Avoid: Android-Only Library in Common
```kotlin
// ❌ Don't do this
commonMain.dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Android-only!
}

// ✅ Use KMP alternative instead
commonMain.dependencies {
    implementation(libs.ktor.client.core) // Works on all platforms
}
```

## Security Considerations

- Never commit API keys or secrets
- Use gradle.properties (git-ignored) for sensitive values
- Consider using platform keystores for credentials
- Regularly update dependencies for security patches
