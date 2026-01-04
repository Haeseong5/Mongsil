// iOS 앱 모듈은 Xcode에서 관리됩니다.
// 이 파일은 Gradle 프로젝트 구조를 위해 필요합니다.

tasks.register("build") {
    dependsOn(":composeApp:embedAndSignAppleFrameworkForXcode")
}
