# 몽실 KMP - 네트워크 API 가이드

KMP 프로젝트에서 Ktor를 사용한 네트워크 통신 가이드입니다.

## 아키텍처

```
API Layer (Ktor Client)
    ↓
Repository Layer
    ↓
ViewModel / UseCase
    ↓
UI Layer
```

## 구조

### 1. Response 모델
```kotlin
@Serializable
data class EmoticonResponse(
    val id: Int,
    val imageUrl: String,
    val title: String,
    val textColor: String,
    val backgroundColor: String
)
```

### 2. API 클라이언트 (Ktor)
```kotlin
class EmoticonApi(private val client: HttpClient) {
    suspend fun getEmoticons(): Result<List<EmoticonResponse>> {
        return try {
            val emoticons = client.get("$BASE_URL/api/emoticon.json")
                .body<List<EmoticonResponse>>()
            Result.success(emoticons)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
```

### 3. Repository
```kotlin
class EmoticonRepository(private val emoticonApi: EmoticonApi) {
    suspend fun getEmoticons(): Result<List<EmoticonResponse>> = 
        withContext(Dispatchers.Default) {
            emoticonApi.getEmoticons()
        }
}
```

## 사용 예제

### ViewModel에서 사용

```kotlin
class MyViewModel(
    private val emoticonRepository: EmoticonRepository
) : ViewModel() {
    
    private val _emoticons = MutableStateFlow<List<EmoticonResponse>>(emptyList())
    val emoticons = _emoticons.asStateFlow()
    
    fun loadEmoticons() {
        viewModelScope.launch {
            emoticonRepository.getEmoticons()
                .onSuccess { emoticons ->
                    _emoticons.value = emoticons
                }
                .onFailure { error ->
                    // 에러 처리
                    println("Error: ${error.message}")
                }
        }
    }
}
```

### Koin 주입

```kotlin
// ViewModel 모듈에서
internal val myFeatureModule = module {
    viewModel { MyViewModel(get()) }
}

// Screen에서
@Composable
fun MyScreen(
    viewModel: MyViewModel = koinInject()
) {
    val emoticons by viewModel.emoticons.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.loadEmoticons()
    }
    
    // UI 렌더링
}
```

## 사용 가능한 API

### 1. EmoticonApi
- **getEmoticons()**: 15개의 감정 이모티콘 목록 조회
                  - **Endpoint**: `https://haeseong5.github.io/api/emoticon.json`

### 2. PosterApi
- **getAllPosters()**: 명언 포스터 목록 조회
- **Endpoint**: `https://haeseong5.github.io/api/saying.json`

## 에러 처리

API는 `Result<T>` 타입을 반환하므로 다음과 같이 처리합니다:

```kotlin
when (val result = repository.getEmoticons()) {
    is Result.Success -> {
        val data = result.getOrNull()
        // 성공 처리
    }
    is Result.Failure -> {
        val error = result.exceptionOrNull()
        // 에러 처리
    }
}

// 또는 확장 함수 사용
result.onSuccess { data ->
    // 성공 처리
}.onFailure { error ->
    // 에러 처리
}

// 또는 기본값 제공
val emoticons = result.getOrElse { emptyList() }
```

## 마이그레이션 노트

### Core 모듈 (Retrofit) → KMP (Ktor)

**변경 사항:**
- Retrofit → Ktor Client
- Hilt → Koin
- Android only → Multiplatform (Android + iOS)

**동일하게 유지:**
- Response 모델 구조
- Repository 패턴
- suspend 함수 사용

**장점:**
- iOS와 Android에서 동일한 네트워크 코드 사용
- 플랫폼별 엔진 자동 선택 (Android: OkHttp, iOS: Darwin)
- 타입 안전성 유지
