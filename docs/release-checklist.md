# 앱 출시 전 종합 점검 보고서

**작성일**: 2026-03-26
**브랜치**: kmp-migration
**버전**: 2.0.0 (KMP 첫 출시)

---

## CRITICAL — 출시 전 반드시 수정

### 1. ~~fallbackToDestructiveMigration — 유저 데이터 전체 삭제 위험~~ (완료)

- **파일**: `PlatformModule.android.kt:40`, `PlatformModule.ios.kt:41`, `PlatformModule.desktop.kt:39`
- ~~모든 플랫폼에서 `.fallbackToDestructiveMigration(dropAllTables = true)` 사용 중~~
- ~~향후 스키마 변경 시 마이그레이션 누락하면 모든 유저의 일기 데이터가 삭제됨~~
- **상태**: 완료 (커밋 `3dcb57a`)

### 2. ~~google-services.json Git 추적 제거~~ (완료)

- `.gitignore`에 `google-services.json` 추가 완료
- `git rm --cached`로 Git 추적 해제 완료
- **주의**: Firebase 콘솔에서 API 키 재발급 또는 제한 설정 필요
- **상태**: 완료

### 3. 백업/복원 시 사진 데이터 유실

- **파일**: `DefaultBackupRepository.kt:142-153`
- `toBackupDiary()`에서 `photoUri`를 포함하지 않음
- 복원 시 `saveDiaryFromBackup()`도 `photoUri`를 전달하지 않음
- **결과**: 백업 → 복원하면 모든 사진 첨부가 사라짐
- **상태**: 미수정

### 4. ~~ViewModel 예외 처리 전무 — 앱 크래시~~ (완료)

- ~~모든 ViewModel에서 `viewModelScope.launch {}` 내부에 try-catch 없음~~
- `BaseViewModel` + `CoroutineExceptionHandler` + `ObserveErrorEffect` 적용
- **상태**: 완료 (커밋 `3da3c24`)

### 5. AdMob 테스트 ID 하드코딩 — 수익 0원

- `BannerAdView.android.kt:17` — `ca-app-pub-3940256099942544/6300978111` (테스트 ID)
- `ShowRewardedAd.android.kt:15` — `ca-app-pub-3940256099942544/5224354917` (테스트 ID)
- `AndroidManifest.xml:24` — 테스트 앱 ID
- **수정**: 실제 AdMob 콘솔 ID로 교체 필요
- **상태**: 미수정

---

## HIGH — 출시 전 수정 권장

### 6. CalendarScreen — `koinInject()` 대신 `koinViewModel()` 사용해야 함

- **파일**: `CalendarScreen.kt:62`
- `koinInject()`는 ViewModelStore 스코프가 아니므로 화면 돌아갈 때 상태 유실 가능
- **상태**: 미수정

### 7. DiaryWriteViewModel — 저장 실패 시 사용자에게 알림 없음

- **파일**: `DiaryWriteViewModel.kt:401-404`
- `onFailure`에서 `isSaving = false`만 하고 에러 표시 없음
- **상태**: 미수정

### 8. AbstractLocalPreferences — HashMap 스레드 안전하지 않음

- **파일**: `AbstractLocalPreferences.kt:13`
- `HashMap`에 멀티스레드 접근 시 `ConcurrentModificationException` 가능
- **수정**: `ConcurrentHashMap` 또는 `Mutex` 사용
- **상태**: 미수정

### 9. R8/ProGuard 비활성화 — APK 크기 및 보안

- **파일**: `composeApp/build.gradle.kts:195`
- `isMinifyEnabled = false` → 코드 난독화 없음, APK 크기 비대
- `proguard-rules.pro` 파일도 없음 (Koin, Ktor, Room, kotlinx.serialization 규칙 필요)
- **상태**: 미수정

### 10. 하드코딩 한국어 문자열 다수 — 다국어 대응 불가

- `AppLockGate.kt`, `DiaryWriteScreen.kt`, `DaysOfWeekTitle.kt`, `MainNavHost.kt` 등
- 최근 다국어 커밋(`52d4cda`)과 모순됨
- **상태**: 미수정

### 11. 릴리즈 빌드에 `println` 디버그 로그 남아있음

- `CalendarViewModel.kt:49,58` — 이모티콘 전체 리스트 출력
- `HttpClientFactory.kt:36` — HTTP 요청/응답 전부 로그 출력
- **상태**: 미수정

### 12. iOS 이미지 피커 미구현

- **파일**: `ImagePicker.ios.kt:9` — `TODO: iOS 이미지 피커 연동`
- iOS에서 사진 첨부 기능이 동작하지 않음
- **상태**: 미수정

### 13. android:allowBackup="true" — 민감 데이터 ADB 백업 노출

- **파일**: `composeApp/src/androidMain/AndroidManifest.xml:14`
- 일기 앱의 특성상 민감한 개인 데이터가 ADB 백업에 포함될 수 있음
- **상태**: 미수정

---

## MEDIUM — 출시 후 빠른 시일 내 수정

| #  | 이슈                                                  | 파일                           | 상태  |
|----|-----------------------------------------------------|------------------------------|-----|
| 14 | DiarySearchViewModel — 전체 일기를 메모리에 로드 (OOM 위험)      | `DiarySearchViewModel.kt:20` | 미수정 |
| 15 | DiaryChartViewModel.loadStreak() — 전체 일기 로드         | `DiaryChartViewModel.kt:109` | 미수정 |
| 16 | CounterViewModel — 빠른 탭 시 저장 순서 꼬임 (race condition) | `CounterViewModel.kt:50-63`  | 미수정 |
| 17 | PasswordHasher — FNV-1a 비암호학적 해시 사용                 | `PasswordHasher.kt:4-9`      | 미수정 |
| 18 | AndroidPdfExportService — Bitmap `recycle()` 안 함    | `AndroidPdfExportService.kt` | 미수정 |
| 19 | kotlinx-coroutines-test가 `implementation`으로 선언됨     | `app/build.gradle.kts:127`   | 미수정 |
| 20 | Koin `4.2.0-beta4` 베타 버전 사용 중                       | `libs.versions.toml:23`      | 미수정 |
| 21 | iOS 버전 `1.0` — Android `2.0.0`과 불일치                 | `Info.plist`                 | 미수정 |
| 22 | DiaryRepository에 죽은 `getEmoticons()` 코드 (ID 불일치)    | `DiaryRepository.kt:58`      | 미수정 |
| 23 | CounterViewModel — 커스텀 CoroutineScope 누수            | `CounterViewModel.kt:21`     | 미수정 |

---

## LOW — 코드 품질

| #  | 이슈                                          | 상태  |
|----|---------------------------------------------|-----|
| 24 | `Pair` 사용 (`shiftMonth`) → 별도 data class 필요 | 미수정 |
| 25 | Repository들이 interface가 아닌 concrete class   | 미수정 |
| 26 | CalendarUiState 기본 날짜가 `2025-01-01` 하드코딩    | 미수정 |
| 27 | 다수 TODO 주석 미해결                              | 미수정 |

---

## 출시 전 체크리스트

- [x] `fallbackToDestructiveMigration` 제거
- [x] `google-services.json` gitignore 처리
- [x] ViewModel launch 블록에 에러 처리 추가
- [ ] AdMob ID를 실제 ID로 교체
- [ ] 백업/복원에 photoUri 포함
- [ ] `isMinifyEnabled = true` + ProGuard 규칙 작성
- [ ] 하드코딩 문자열 리소스화
- [ ] `println` 제거 또는 로거로 교체
- [ ] 릴리즈 빌드로 실제 기기 테스트
- [ ] 크래시 모니터링 (Firebase Crashlytics) 정상 동작 확인
