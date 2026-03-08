# Mongsil 프로젝트 Claude Code 룰

## 아키텍처 원칙 (Clean Architecture)

- 레이어 의존성 방향: UI → ViewModel → UseCase → Repository → DataSource
- 레이어 간 의존성 역전: 상위 레이어는 하위 레이어의 interface에만 의존
- Domain 레이어는 Android 의존성 금지 (순수 Kotlin만 허용)
- 각 레이어 간 데이터 전달은 반드시 별도 Model 클래스 사용 (DTO, DomainModel, UiState 분리)

## Composable 규칙

- Screen Composable은 ViewModel에 직접 의존 금지
  → 상태(UiState)와 이벤트 콜백(onEvent)만 파라미터로 받기
- Preview는 반드시 작성, UiState 목업 데이터 사용
- 슬롯 파라미터로 전달되는 람다 Composable은 반드시 별도 함수로 분리
- Composable 중첩 depth는 깊어지지 않게 3단계 초과 시, 별도 컴포저블로 분리

## 테스트 규칙

- UseCase, ViewModel은 반드시 Unit Test 작성
- 테스트 함수명: `테스트대상_조건_기대결과` 형식
- Repository는 반드시 interface로 선언하여 Fake 구현체로 테스트
- Mockito 대신 Fake 클래스 구현 선호

## 코드 스타일 규칙

- 함수 길이 20줄 이하 유지
- 함수 파라미터 3개 초과 시 data class로 묶기
- when문에서 else 브랜치 최대한 지양 (sealed class 활용)
- 널 안정성: !! 연산자 사용 금지, elvis 연산자(?:) 또는 let 활용
- 하드코딩 문자열 금지, 반드시 strings.xml 또는 상수로 분리
- Pair, Triple 사용 대신 별도 데이터 클래스를 선언하는 방식을 써.
- 문자열은 스트링리소스에 선언해줘

## 금지 사항

- God Class (한 클래스가 너무 많은 책임) 금지
- 비즈니스 로직을 Composable 안에 직접 작성 금지
- 전역 상태(companion object var 등) 남용 금지
- 미사용 주석 코드 커밋 금지