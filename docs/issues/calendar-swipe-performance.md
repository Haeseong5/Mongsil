# [Issue] 캘린더 스와이프 버벅임 (CalendarScreen)

- **발견일**: 2026-03-08
- **상태**: 미수정
- **심각도**: Medium
- **관련 파일**
    - `composeApp/src/commonMain/.../screen/calendar/CalendarScreen.kt`
    - `composeApp/src/commonMain/.../screen/calendar/component/CalendarMonthContent.kt`
    - `composeApp/src/commonMain/.../screen/calendar/CalendarViewModel.kt`

---

## 증상

`CalendarScreen`에서 월별 캘린더를 좌우로 스와이프할 때 약간의 버벅거림 발생.

---

## 원인 분석

### 문제 1 — O(n²) 리스트 탐색 `CalendarMonthContent.kt:81-87` ⚠️ 가장 심각

`HorizontalPager` 내부 `items` 블록(42칸)에서 날짜마다 `calendarRecords`를 두 번 순회.

```kotlin
// 42번 반복되는 items 블록 안에서:
isRecord = uiState.calendarRecords.any { it.date == date },   // O(n)
emoticonImageUrl = uiState.emoticons
    .find { emoticon ->
        uiState.calendarRecords
            .lastOrNull { it.date == date }                   // O(n) 또 탐색!
            ?.emotionId == emoticon.id
    }?.imageUrl ?: "",
```

- 날짜 42칸 × `calendarRecords` 2회 순회 = **매 렌더링마다 84n 번 탐색**
- 일기 데이터가 많을수록 더 심각해짐

**해결 방향**: `calendarRecords`를 `Map<LocalDate, CalendarRecord>`으로 변환해 O(1) 조회

```kotlin
val recordMap = remember(uiState.calendarRecords) {
    uiState.calendarRecords.associateBy { it.date }
}
// 이후 recordMap[date] 로 O(1) 접근
```

---

### 문제 2 — 스와이프 시 불필요한 `animateScrollToPage` 재호출 `CalendarScreen.kt:129-154`

스와이프 한 번에 다음 흐름이 발생:

```
스와이프
  → snapshotFlow (page 변경 감지)
    → onYearMonthChange(year, month)
      → ViewModel: uiState.currentYear/Month 업데이트
        → LaunchedEffect(uiState.currentYear, uiState.currentMonth) 재실행
          → animateScrollToPage(이미 현재 페이지) ← 불필요한 호출
```

`animateScrollToPage`가 이미 위치한 페이지로 매번 재호출됨.

**해결 방향**: 현재 페이지와 동일한 경우 skip

```kotlin
LaunchedEffect(uiState.currentYear, uiState.currentMonth) {
    val targetPage = initialPage + yearDiff * 12 + monthDiff
    if (targetPage != pagerState.currentPage && targetPage in 0 until totalPages) {
        pagerState.animateScrollToPage(targetPage)
    }
}
```

---

### 문제 3 — `remember` 없는 `calendarDays` 재계산 `CalendarMonthContent.kt:48-62`

```kotlin
// remember 없음 → 리컴포지션마다 매번 리스트 재생성
val calendarDays = buildList {
    repeat(startDayOfWeek) { add(null) }
    for (day in 1..daysInMonth) {
        add(LocalDate(year, month, day))
    }
    ...
}
```

`uiState`가 변경될 때마다 (calendarRecords, emoticons 업데이트 등) 불필요하게 재계산됨.

**해결 방향**: `year`, `month`가 바뀔 때만 재계산

```kotlin
val calendarDays = remember(year, month) {
    buildList { ... }
}
```

---

## 수정 우선순위

| 순위 | 문제                          | 예상 효과         |
|----|-----------------------------|---------------|
| 1  | O(n²) 탐색 → Map 변환           | 렌더링 속도 직접 개선  |
| 2  | `animateScrollToPage` 조건 추가 | 불필요한 애니메이션 제거 |
| 3  | `calendarDays` remember 추가  | 리컴포지션 최적화     |