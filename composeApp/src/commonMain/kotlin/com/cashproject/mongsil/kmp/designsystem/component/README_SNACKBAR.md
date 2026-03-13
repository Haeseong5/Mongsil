# Snackbar 사용 가이드

전역 스넥바 시스템을 사용하여 각 화면에서 쉽게 스넥바를 표시할 수 있습니다.

## 기본 사용법

### 1. 스넥바 컨트롤러 가져오기

```kotlin
@Composable
fun YourScreen() {
    val snackbarController = rememberSnackbarController()
    
    // ... 나머지 코드
}
```

### 2. 스넥바 표시하기

#### 기본 메시지
```kotlin
snackbarController.showSnackbar("저장되었습니다.")
```

#### 액션 버튼이 있는 스넥바
```kotlin
snackbarController.showSnackbar(
    message = "삭제되었습니다.",
    actionLabel = "취소",
    onAction = {
        // 취소 버튼 클릭 시 동작
        restoreItem()
    }
)
```

#### 긴 표시 시간
```kotlin
snackbarController.showSnackbar(
    message = "중요한 메시지입니다.",
    duration = SnackbarDuration.Long
)
```

## 실제 사용 예제

### ViewModel에서 Side Effect로 사용

```kotlin
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinInject()
) {
    val snackbarController = rememberSnackbarController()
    
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is MySideEffect.ShowSuccess -> {
                    snackbarController.showSnackbar(effect.message)
                }
                is MySideEffect.ShowError -> {
                    snackbarController.showSnackbar(
                        message = effect.message,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }
}
```

### 버튼 클릭 시 직접 표시

```kotlin
@Composable
fun MyScreen() {
    val snackbarController = rememberSnackbarController()
    
    Button(
        onClick = {
            snackbarController.showSnackbar("버튼이 클릭되었습니다.")
        }
    ) {
        Text("클릭")
    }
}
```

## API 레퍼런스

### SnackbarController.showSnackbar()

```kotlin
fun showSnackbar(
    message: String,                              // 표시할 메시지 (필수)
    actionLabel: String? = null,                   // 액션 버튼 라벨 (선택)
    duration: SnackbarDuration = SnackbarDuration.Short,  // 표시 시간
    onAction: (() -> Unit)? = null                 // 액션 버튼 클릭 콜백
)
```

### SnackbarDuration 옵션

- `SnackbarDuration.Short` - 짧게 표시 (기본값, 약 4초)
- `SnackbarDuration.Long` - 길게 표시 (약 10초)
- `SnackbarDuration.Indefinite` - 무한정 표시 (사용자가 닫을 때까지)

## 주의사항

1. **CompositionLocal 범위**: `rememberSnackbarController()`는 `MainScreen` 내부에서만 사용 가능합니다.
2. **비동기 호출**: `showSnackbar()`는 내부적으로 코루틴을 사용하므로 별도의 `LaunchedEffect`나 코루틴 스코프가 필요하지 않습니다.
3. **중복 호출**: 여러 스넥바를 동시에 호출하면 큐에 쌓여 순차적으로 표시됩니다.
