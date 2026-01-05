# Architecture Guidelines

## Project Architecture

This project follows a **multi-module architecture** with clear separation of concerns.

### Module Structure

```
Mongsil/
├── composeApp/          # KMP shared application code
│   ├── commonMain/      # Shared UI and business logic
│   ├── androidMain/     # Android platform-specific code
│   └── iosMain/         # iOS platform-specific code
├── app/                 # Android application module
├── core/                # Core shared modules
│   ├── common/          # Common utilities and base classes
│   ├── database/        # Database layer
│   ├── network/         # Network layer
│   └── repository/      # Repository pattern implementations
└── feature/             # Feature modules
    └── backup/          # Backup feature
```

## Layered Architecture

### 1. Presentation Layer
- **Location**: `composeApp/commonMain` (for shared UI)
- **Responsibility**: UI components, ViewModels, UI state
- **Dependencies**: Can depend on domain and data layers

### 2. Domain Layer
- **Location**: `core/repository/`, feature modules
- **Responsibility**: Business logic, use cases, repository interfaces
- **Dependencies**: Should not depend on framework-specific code

### 3. Data Layer
- **Location**: `core/database/`, `core/network/`
- **Responsibility**: Data sources, API clients, database operations
- **Dependencies**: Can use platform-specific APIs

## Design Patterns

### Repository Pattern
- Create repository interfaces in domain layer
- Implement repositories in data layer
- Repositories abstract data sources from business logic

```kotlin
// Domain layer - Interface
interface UserRepository {
    suspend fun getUser(id: String): Result<User>
    suspend fun saveUser(user: User): Result<Unit>
}

// Data layer - Implementation
class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val localDataSource: UserLocalDataSource
) : UserRepository {
    override suspend fun getUser(id: String): Result<User> {
        // Implementation with caching logic
    }
}
```

### ViewModel Pattern
- Use ViewModels for managing UI state
- ViewModels should not contain Android/iOS specific code in common module
- Expose UI state as StateFlow or State for Compose

```kotlin
class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    fun loadUser(id: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when (val result = userRepository.getUser(id)) {
                is Result.Success -> _uiState.value = UiState.Success(result.data)
                is Result.Error -> _uiState.value = UiState.Error(result.exception)
            }
        }
    }
}
```

### Dependency Injection
- Use constructor injection for better testability
- Consider using Koin for multiplatform DI
- Keep dependencies explicit and avoid service locator pattern

## Best Practices

### State Management
- Use unidirectional data flow (UDF)
- Expose immutable state from ViewModels
- Handle side effects explicitly

### Error Handling
- Use Result/Either types for operation results
- Provide meaningful error messages
- Log errors appropriately for debugging

### Async Operations
- Use Kotlin coroutines for async operations
- Prefer `suspend` functions over callbacks
- Handle cancellation properly

### Testing
- Write unit tests for ViewModels and repositories
- Mock external dependencies
- Test error cases and edge conditions
