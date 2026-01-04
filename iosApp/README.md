# iOS App - 몽실 KMP

Kotlin Multiplatform으로 구현된 몽실 앱의 iOS 버전입니다.

## 🚀 실행 방법

### 1. Xcode에서 프로젝트 열기
```bash
open iosApp/iosApp.xcodeproj
```

또는 Xcode에서 직접:
- **File → Open...**
- `iosApp/iosApp.xcodeproj` 선택

### 2. 시뮬레이터에서 실행
1. Xcode 상단에서 타겟 선택 (iPhone 14 Pro 등)
2. **⌘R** (Command + R) 또는 ▶️ 버튼 클릭

### 3. 실제 기기에서 실행
1. `Configuration/Config.xcconfig` 파일 열기
2. `TEAM_ID`를 본인의 Apple Developer Team ID로 변경
3. Xcode에서 Signing & Capabilities 설정
4. 기기 연결 후 실행

## 📁 프로젝트 구조

```
iosApp/
├── Configuration/
│   └── Config.xcconfig          # 앱 설정 (Bundle ID, Team ID 등)
├── iosApp/
│   ├── iOSApp.swift             # 앱 진입점
│   ├── ContentView.swift        # SwiftUI 메인 화면
│   ├── Info.plist               # iOS 앱 정보
│   └── Assets.xcassets/         # 아이콘 및 리소스
└── iosApp.xcodeproj/            # Xcode 프로젝트
```

## 🔗 Kotlin Framework 연결

이 iOS 앱은 **composeApp** 모듈의 Kotlin 코드를 사용합니다.

- **Framework 이름**: `ComposeApp`
- **빌드 위치**: `../composeApp/build/xcode-frameworks/`
- **자동 빌드**: Xcode Build Phase에서 Gradle 태스크 실행

## ⚙️ 설정 변경

### Bundle ID 변경
`Configuration/Config.xcconfig`에서:
```
BUNDLE_ID=com.cashproject.mongsil.kmp
```

### 앱 이름 변경
```
APP_NAME=몽실
```

## 🐛 문제 해결

### Framework not found 에러
```bash
cd ..
./gradlew :composeApp:embedAndSignAppleFrameworkForXcode
```

### 시뮬레이터 빌드 실패
- Xcode 버전 확인 (최소 15.0 권장)
- iOS Deployment Target: 14.0 이상

## 📝 참고사항

- 첫 빌드는 Kotlin Framework 생성으로 시간이 걸릴 수 있습니다
- Xcode 빌드 시 자동으로 Gradle 태스크가 실행됩니다
- Android Studio에서 코드 수정 후 Xcode에서 Clean Build 권장
