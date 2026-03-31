# Mongsil 프로젝트 문서

프로젝트 관련 문서를 마크다운으로 관리합니다.

## 폴더 구조

```
docs/
├── README.md          # 이 파일 (문서 목록)
├── issues/            # 버그 및 성능 이슈
└── features/          # 기능 아이디어 및 기획
```

## 문서 목록

### Release

| 파일                                           | 제목               | 날짜         |
|----------------------------------------------|------------------|------------|
| [release-checklist.md](release-checklist.md) | 앱 출시 전 종합 점검 보고서 | 2026-03-26 |

### Issues

| 파일                                                                    | 제목           | 상태  | 날짜         |
|-----------------------------------------------------------------------|--------------|-----|------------|
| [calendar-swipe-performance.md](issues/calendar-swipe-performance.md) | 캘린더 스와이프 버벅임 | 미수정 | 2026-03-08 |
| [db-backup-version-compatibility.md](issues/db-backup-version-compatibility.md) | DB 백업 파일 버전 호환성 | 참고 | 2026-03-31 |

### Features

| 파일                                                                | 제목                 | 날짜         |
|-------------------------------------------------------------------|--------------------|------------|
| [motivation-reward-ideas.md](features/motivation-reward-ideas.md) | 동기부여 & 보상 기능 아이디어  | 2026-03-08 |
| [monetization-ideas.md](features/monetization-ideas.md)           | 수익화 아이디어           | 2026-03-08 |
| [statistics-ideas.md](features/statistics-ideas.md)               | 통계 기능 아이디어         | 2026-03-14 |
| [ai-diary-feedback.md](features/ai-diary-feedback.md)             | AI 일기 피드백 기능       | 2026-03-16 |
| [emoticon-expansion.md](features/emoticon-expansion.md)           | 감정 이모티콘 확장 및 복수 선택 | 2026-03-29 |

---

## 작성 규칙

- **이슈 파일명**: `{기능}-{현상}.md` (예: `calendar-swipe-performance.md`)
- **상태**: `미수정` / `수정중` / `완료`
- **위 목록을 항상 최신으로 유지**