# Compose Preview Guidelines

Mongsil 프로젝트의 Compose Preview 작성 규칙입니다.

---

## 필수 규칙

### 1. 반드시 MongsilTheme를 사용할 것

- 모든 `@Preview` 함수는 **반드시** `MongsilTheme`로 감싸야 합니다.
- `@Preview` 어노테이션을 반드시 추가해야 합니다.
- `showBackground = true` 옵션을 권장합니다.

✅ 올바른 예시
```kotlin
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MongsilTheme {
        HomeScreen(
            uiState = HomeUiState(
                userName = "홍길동",
                totalRecords = 42,
                recentEmotion = "행복"
            )
        )
    }
}
```

❌ 잘못된 예시
```kotlin
// @Preview 어노테이션 누락
@Composable
fun HomeScreenPreview() {
    HomeScreen(...)
}

// MongsilTheme 누락
@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(...)
}
```

### 2. Mock 데이터를 성의있게 만들 것

- 빈 값, null, "test", "placeholder" 같은 의미없는 데이터 사용 금지
- 실제 사용될 법한 현실적인 데이터를 사용
- 리스트는 1~3개 정도의 의미있는 항목으로 구성

✅ 올바른 예시
```kotlin
@Preview(showBackground = true)
@Composable
fun CalendarListPreview() {
    MongsilTheme {
        CalendarList(
            records = listOf(
                CalendarRecord(
                    date = LocalDate(2026, 2, 1),
                    emotion = "기쁨",
                    content = "프로젝트 마감을 무사히 완료했다"
                ),
                CalendarRecord(
                    date = LocalDate(2026, 2, 3),
                    emotion = "평온",
                    content = "친구들과 저녁 식사"
                )
            )
        )
    }
}
```

❌ 잘못된 예시
```kotlin
@Preview(showBackground = true)
@Composable
fun CalendarListPreview() {
    MongsilTheme {
        CalendarList(
            records = listOf(
                CalendarRecord(
                    date = LocalDate(2026, 1, 1),
                    emotion = "test",
                    content = "test content"
                )
            )
        )
    }
}
```

---

## 요약

1. **@Preview + MongsilTheme 필수**
2. **성의있는 Mock 데이터 사용**