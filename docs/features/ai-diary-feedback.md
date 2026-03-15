# AI 일기 피드백 기능

## 개요

유저가 작성한 일기 내용(`content`)과 선택한 감정(`selectedEmoticon`)을 기반으로 AI가 따뜻한 피드백을 제공하는 기능.
온디바이스 AI를 사용해 네트워크 없이도 동작하고 프라이버시를 보호한다.

---

## 선택된 온디바이스 AI 방식: MediaPipe LLM Inference API

### 비교 검토

| 방법 | 지원 기기 | 유연성 | 한국어 지원 |
|---|---|---|---|
| **MediaPipe LLM Inference** | Android GPU 탑재 기기 (대부분) | ✅ 높음 | ✅ Gemma 3 지원 |
| Gemini Nano (AICore) | Pixel 8+ 일부 기기만 | ❌ 제한적 | ✅ |
| ML Kit Smart Reply | 모든 기기 | ❌ 고정된 응답만 | ⚠️ |

### 결론: `com.google.mediapipe:tasks-genai` + Gemma 3 1B INT4 모델
- 모델 크기: ~600MB (INT4 양자화)
- 기기 요건: Android 10+, GPU 탑재 (대부분의 현대 기기)
- 완전 오프라인 동작
- Gemma 3 1B는 한국어 지원 양호

---

## 아키텍처 설계

### 레이어 구조

```
[androidMain]
MediaPipeAiFeedbackDataSource
    implements ↓
[commonMain]
AiFeedbackDataSource (interface)
    ↑
AiFeedbackRepository (interface + impl)
    ↑
GetAiFeedbackUseCase
    ↑
DiaryWriteViewModel (기존 ViewModel에 통합)
    ↑
DiaryWriteScreen (기존 화면에 UI 추가)
```

### iOS
- Stub 구현 (`isAvailable() = false`, 빈 문자열 반환)
- 추후 CoreML로 교체 가능

---

## 구현 계획

### 1. 의존성 추가
**파일:** `composeApp/build.gradle.kts`
```kotlin
androidMain.dependencies {
    implementation("com.google.mediapipe:tasks-genai:0.10.22")
}
```

### 2. 공통 인터페이스 (commonMain)
- `kmp/core/ai/AiFeedbackDataSource.kt` — 새 파일
- `kmp/core/ai/AiFeedbackRepository.kt` — 새 파일
- `kmp/core/ai/AiFeedbackRepositoryImpl.kt` — 새 파일

### 3. Android 구현 (androidMain)
- `kmp/core/ai/MediaPipeAiFeedbackDataSource.kt` — 새 파일
- 모델은 최초 피드백 요청 시 lazy 초기화
- 모델 파일: 앱 최초 실행 시 다운로드 (~600MB), Wi-Fi 권장 안내

### 4. iOS Stub (iosMain)
- `kmp/core/ai/IosAiFeedbackDataSource.kt` — 새 파일

### 5. UseCase
- `kmp/domain/usecase/GetAiFeedbackUseCase.kt` — 새 파일
- 빈 content → `Result.failure`
- 정상 content → `repository.getFeedback(...)` 위임

### 6. UiState / Event 확장
`DiaryWriteUiState`에 추가:
```kotlin
val aiFeedback: String? = null
val isAiFeedbackLoading: Boolean = false
val isAiAvailable: Boolean = false
```

`DiaryWriteEvent`에 추가:
```kotlin
data object RequestAiFeedback : DiaryWriteEvent()
data object DismissAiFeedback : DiaryWriteEvent()
```

### 7. UI 추가 (DiaryWriteScreen.kt)
- content가 있을 때만 "AI 피드백 받기" 버튼 노출
- 피드백 결과는 BottomSheet로 표시
- 로딩 중: 애니메이션 스피너
- 미지원 기기(`isAiAvailable = false`): 버튼 비노출

### 8. DI 등록
```kotlin
// commonMain
single<AiFeedbackRepository> { AiFeedbackRepositoryImpl(get()) }
single { GetAiFeedbackUseCase(get()) }

// androidMain
single<AiFeedbackDataSource> { MediaPipeAiFeedbackDataSource(androidContext()) }
```

---

## 모델 배포 전략

| 전략 | 설명 |
|---|---|
| 런타임 다운로드 (권장) | 앱 번들 크기 유지, 최초 사용 시 Wi-Fi 권장 후 다운로드 |
| WorkManager 사전 다운로드 | 백그라운드에서 미리 다운로드 (진행률 표시) |

---

## 검증 방법

1. Android GPU 탑재 에뮬레이터 또는 실기기에서 빌드
2. 일기 내용 + 감정 선택 후 "AI 피드백 받기" 버튼 탭
3. 로딩 상태 → 피드백 텍스트 표시 확인
4. 오프라인 상태에서도 정상 동작 확인
5. content가 비어있을 때 버튼 비노출/비활성 확인
6. `GetAiFeedbackUseCase` 단위 테스트: 빈 content → failure, 정상 content → success

---

## 날짜

2026-03-16
