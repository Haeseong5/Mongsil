# Architecture Guidelines

## Project Architecture

This project uses **Kotlin Multiplatform (KMP)** with all active development in the **composeApp** module. Other modules (`app/`, `core/`, `feature/`) are legacy code and should not be modified.

### Module Structure

```
Mongsil/
├── composeApp/          # KMP shared application code (ACTIVE DEVELOPMENT)
│   ├── commonMain/      # All shared UI and business logic
│   │   ├── screen/      # Feature screens (MVI pattern)
│   │   ├── di/          # Dependency injection modules
│   │   ├── network/     # Network layer (API, models)
│   │   ├── repository/  # Repository implementations
│   │   ├── database/    # Database layer
│   │   ├── designsystem/# Design system (colors, theme, typography)
│   │   └── model/       # Shared data models
│   ├── androidMain/     # Android platform-specific code
│   └── iosMain/         # iOS platform-specific code
├── app/                 # [LEGACY] Android application module
├── core/                # [LEGACY] Core shared modules
└── feature/             # [LEGACY] Feature modules
```

## Directory-Based Architecture

Since the project is not yet modularized, we use **directory separation** within `composeApp/commonMain` to maintain clear boundaries:

### 1. Presentation Layer (`screen/`)
- **Location**: `composeApp/src/commonMain/kotlin/.../screen/`
- **Structure per feature**:
  ```
  screen/
  └── featurename/
      ├── FeatureScreen.kt      # Composable UI
      ├── FeatureViewModel.kt   # Business logic & state management
      ├── model/                # Feature-specific models (UiState, etc.)
      ├── component/            # Reusable UI components
      └── utils/                # Feature-specific utilities
  ```
- **Responsibility**: UI components, ViewModels, UI state (MVI pattern)
- **Dependencies**: Can depend on repository, network, and database layers

### 2. Data Layer
- **Repository** (`repository/`): Repository pattern implementations
- **Network** (`network/`): API clients, network models, HTTP configuration
- **Database** (`database/`): Database drivers and operations
- **Responsibility**: Data sources, API clients, database operations
- **Dependencies**: Platform-agnostic, uses KMP libraries

### 3. Cross-Cutting Concerns
- **DI** (`di/`): Koin modules for dependency injection
- **Design System** (`designsystem/`): Shared UI components, theme, colors, typography
- **Model** (`model/`): Shared domain models used across features

## Design Patterns

### MVI Pattern (Model-View-Intent)

The project follows **MVI architecture** for all screens:

#### Model: UiState
- Immutable data class representing the entire UI state
- Located in `screen/featurename/model/`
- Contains all data needed to render the UI

```kotlin
// Example: CalendarUiState.kt
data class CalendarUiState(
    val currentYear: Int = 2025,
    val currentMonth: Int = 1,
    val calendarRecords: List<CalendarRecord> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
```

#### View: Composable Screen
- Pure composable functions that render UI based on UiState
- Located in `screen/featurename/FeatureScreen.kt`
- Collects state from ViewModel and emits user intents

```kotlin
// Example: CalendarScreen.kt
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = koinInject(),
    onNavigateTo: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    CalendarScreenContent(
        uiState = uiState,
        onDateClick = viewModel::onDateClick,
        onYearMonthChange = viewModel::updateYearMonth
    )
}
```

#### Intent: ViewModel Methods
- Public methods in ViewModel represent user intents
- Update state by calling `_uiState.update { }` or `_uiState.value = `
- Handle business logic and coordinate with repositories

```kotlin
// Example: CalendarViewModel.kt
class CalendarViewModel(
    private val repository: CalendarRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()
    
    // Intent: User wants to change the month
    fun updateYearMonth(year: Int, month: Int) {
        _uiState.update { it.copy(currentYear = year, currentMonth = month) }
        loadDiariesForMonth(year, month)
    }
    
    // Intent: User clicks on a date
    fun onDateClick(date: LocalDate) {
        // Handle date selection logic
    }
    
    private fun loadDiariesForMonth(year: Int, month: Int) {
        viewModelScope.launch {
            val diaries = repository.getDiariesByYearMonth(year, month)
            _uiState.update { it.copy(calendarRecords = diaries) }
        }
    }
}
```

### Repository Pattern
- Implement repositories directly in `repository/` directory
- No separate interface layer (pragmatic approach for current project size)
- Repositories abstract data sources and handle business logic

```kotlin
// Example: DiaryRepository.kt
class DiaryRepository(
    private val apiService: ApiService,
    private val database: Database
) {
    suspend fun getDiariesByYearMonth(year: Int, month: Int): List<Diary> {
        // Combine API and local database data
        return try {
            apiService.fetchDiaries(year, month)
        } catch (e: Exception) {
            database.getDiaries(year, month)
        }
    }
}
```

### Dependency Injection
- Use **Koin** for multiplatform dependency injection
- Create feature-specific modules in `di/` directory
- Register ViewModels, Repositories, and other dependencies

```kotlin
// Example: CalendarModule.kt
val calendarModule = module {
    viewModel { CalendarViewModel(get(), get()) }
    single { CalendarRepository(get(), get()) }
}
```

## Best Practices

### State Management (MVI)
- **Unidirectional Data Flow (UDF)**: User intents → ViewModel → UiState → UI
- **Immutable State**: Always use `data class` for UiState and create new instances with `.copy()`
- **Single Source of Truth**: UiState is the only source of truth for UI
- **StateFlow**: Use `StateFlow` for exposing state, collect with `collectAsStateWithLifecycle()` in Compose
- **State Updates**: Use `_uiState.update { it.copy(...) }` for atomic state updates

### Screen Organization
Each feature screen should follow this structure:
```
screen/featurename/
├── FeatureScreen.kt          # Main composable (View)
├── FeatureViewModel.kt       # State & business logic (Intent handler)
├── model/
│   └── FeatureUiState.kt     # UI state model (Model)
├── component/                # Feature-specific components
│   ├── ComponentA.kt
│   └── ComponentB.kt
└── utils/                    # Feature-specific utilities
    └── FeatureUtils.kt
```

### Dependency Injection (Koin)
- Create a module file for each feature in `di/` directory
- Use `viewModel { }` for ViewModels, `single { }` for repositories
- Inject dependencies with `koinInject()` in composables
- Use constructor injection in ViewModels and repositories

```kotlin
// In FeatureModule.kt
val featureModule = module {
    viewModel { FeatureViewModel(get()) }
    single { FeatureRepository(get()) }
}

// In Screen
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = koinInject()
) { }
```

### Error Handling
- Use `Result<T>` types for repository operations
- Include error states in UiState (e.g., `error: String?`, `isLoading: Boolean`)
- Handle errors gracefully in UI with error messages or fallback content
- Log errors for debugging

### Async Operations
- Use Kotlin coroutines with `viewModelScope.launch` in ViewModels
- Prefer `suspend` functions over callbacks
- Handle loading states explicitly in UiState
- Use `.onSuccess { }` and `.onFailure { }` for Result handling

### Network & API
- Define API endpoints in `network/api/` directory
- Keep network models in `network/model/` directory
- Transform network models to domain models in repositories
- Handle network errors and timeouts appropriately

### UI Components
- Place reusable components in `designsystem/component/`
- Place feature-specific components in `screen/featurename/component/`
- Use `Modifier` parameter for flexibility
- Follow Compose best practices (remember, derivedStateOf, etc.)

### Testing
- Write unit tests for ViewModels (test state transformations)
- Mock repositories and test business logic
- Test error cases and edge conditions
- Test state updates and intent handling
