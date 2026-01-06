# Ktor 네트워크 통신 가이드

Ktor 3.0.3을 사용한 KMP 네트워크 통신 설정 및 사용법입니다.

## 구조

```
network/
├── HttpClientFactory.kt       # Ktor 클라이언트 생성 (expect/actual)
├── ApiService.kt               # API 서비스 클래스
└── model/
    └── Post.kt                 # 응답 모델 (예제)
```

## 설정

### 1. 플랫폼별 엔진

**Android**: OkHttp
```kotlin
// HttpClientFactory.android.kt
actual fun getPlatformEngine(): HttpClientEngine {
    return OkHttp.create()
}
```

**iOS**: Darwin
```kotlin
// HttpClientFactory.ios.kt
actual fun getPlatformEngine(): HttpClientEngine {
    return Darwin.create()
}
```

### 2. HttpClient 기본 설정

- **JSON 직렬화**: kotlinx.serialization
- **로깅**: 요청/응답 로그 출력
- **에러 처리**: Result 타입 반환

### 3. Koin 등록

```kotlin
val appModule = module {
    single { HttpClientFactory.create() }
    single { ApiService(get()) }
}
```

## 사용 예제

### ViewModel에서 API 호출

```kotlin
class MyViewModel(
    private val apiService: ApiService
) {
    var posts by mutableStateOf<List<Post>>(emptyList())
        private set
    
    var isLoading by mutableStateOf(false)
        private set
    
    var errorMessage by mutableStateOf<String?>(null)
        private set
    
    fun loadPosts() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            
            apiService.getPosts()
                .onSuccess { posts = it }
                .onFailure { errorMessage = it.message }
            
            isLoading = false
        }
    }
}
```

### UI에서 사용

```kotlin
@Composable
fun PostListScreen(viewModel: MyViewModel = koinInject()) {
    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }
    
    when {
        viewModel.isLoading -> {
            CircularProgressIndicator()
        }
        viewModel.errorMessage != null -> {
            Text("Error: ${viewModel.errorMessage}")
        }
        else -> {
            LazyColumn {
                items(viewModel.posts) { post ->
                    PostItem(post)
                }
            }
        }
    }
}
```

## API Service 메서드

### GET 요청
```kotlin
suspend fun getPosts(): Result<List<Post>>
suspend fun getPost(id: Int): Result<Post>
```

### POST 요청
```kotlin
suspend fun createPost(post: Post): Result<Post>
```

## 응답 모델 생성

```kotlin
@Serializable
data class YourModel(
    val id: Int,
    val name: String
)
```

**중요**: `@Serializable` 어노테이션 필수!

## 에러 처리

```kotlin
apiService.getPosts()
    .onSuccess { posts ->
        // 성공 처리
    }
    .onFailure { exception ->
        when (exception) {
            is IOException -> // 네트워크 에러
            is SerializationException -> // JSON 파싱 에러
            else -> // 기타 에러
        }
    }
```

## Base URL 변경

`ApiService.kt`에서 `BASE_URL` 상수를 변경:

```kotlin
companion object {
    private const val BASE_URL = "https://your-api.com"
}
```

## 로깅 레벨 조정

`HttpClientFactory.kt`에서 로그 레벨 변경:

```kotlin
install(Logging) {
    level = LogLevel.ALL  // ALL, HEADERS, BODY, INFO, NONE
}
```

## 참고 자료

- [Ktor Documentation](https://ktor.io/docs/getting-started-ktor-client.html)
- [kotlinx.serialization](https://github.com/Kotlin/kotlinx.serialization)
