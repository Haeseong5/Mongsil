### 📔 Mongsil (몽실)

하루의 감정을 기록하고 돌아볼 수 있는 감정 일기 Android 앱
사용자의 감정 흐름을 시각적으로 정리하고 스스로를 돌아볼 수 있도록 기획·개발한 개인 사이드 프로젝트

---

### 📱 프로젝트 소개

몽실(Mongsil)은 하루 동안 느꼈던 감정과 생각을 간단하게 기록하고
시간의 흐름에 따라 감정 변화를 돌아볼 수 있도록 설계된 감정 기록 앱입니다.

단순한 텍스트 일기가 아닌,

•	오늘의 기분
  
•	감정의 강도
  
•	하루를 대표하는 감정
  

을 중심으로 기록하여 사용자가 자신의 감정을 인식하고 정리할 수 있도록 돕는 것을 목표로 제작되었습니다.

---

### 🗓️ 개발 기간

2023.03 ~ 2023.05

---

### 🧩 기획 배경

•	하루를 어떻게 보냈는지 금방 잊어버리는 문제
•	감정을 기록하고 싶지만 긴 글 작성이 부담되는 점
•	기존 일기 앱의 텍스트 위주 UX의 진입 장벽

이를 해결하기 위해 다음과 같은 방향으로 기획했습니다.

  ✔ 짧은 입력만으로 감정 기록 가능 
  
  ✔ 감정 중심 UI
  
  ✔ 타임라인 형태의 기록 구조
  
  ✔ 매일 부담 없이 사용할 수 있는 UX

---

### ✨ 주요 기능

🔹 감정 일기 기록

•	하루의 기분을 감정으로 선택하여 기록
•	텍스트 메모와 함께 감정 저장 가능

🔹 타임라인 기반 조회

•	날짜 순으로 감정 기록 확인
•	감정 흐름을 한눈에 파악 가능

🔹 감정 수정 및 삭제

•	이미 기록된 감정 일기 수정 지원
•	사용자 중심 데이터 관리 구조

🔹 오프라인 대응

•	Room 기반 로컬 저장소 사용
•	네트워크 연결 없이도 기록 가능

---

👥 사용자 관점 UX 설계

•	긴 글 작성 없이도 감정 표현 가능

•	하루 기록에 대한 심리적 부담 최소화

•	직관적인 아이콘과 색상 중심 UI 구성

•	“매일 쓰는 앱”을 목표로 한 단순한 동선 설계

---

### 🔗 Play Store 링크

👉 https://play.google.com/store/apps/details?id=com.cashproject.mongsil

---

### 🛠️ 기술 스택

•	Language: Kotlin

•	Platform: Android

UI

•	Jetpack Compose

Architecture

•	MVVM Architecture
•	Clean Architecture

Data

•	Room (Local DB)
•	Firebase Firestore

Async

•	Kotlin Coroutines
•	Flow

Firebase

•	Analytics
•	Crashlytics
•	Remote Config
•	FCM

---

### 📊 프로젝트 특징

•	Jetpack Compose 기반 UI 전면 적용

•	상태 기반 UI(State-driven UI) 설계

•	ViewModel 중심 단방향 데이터 흐름

•	감정 데이터 구조 설계 및 모델링

•	실제 서비스 배포를 고려한 앱 구조 구성

---

### 📸 스크린샷

<p align="center">
  <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/1ca92e31-d5cd-4f92-a867-61f7553d350d" width="30%" />
  <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/94cf7613-13a5-4831-b094-53740368b3d9" width="30%" /> 
  <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/e3aa89b0-2be8-42e1-a989-489335665f1e" width="30%" />
</p>

---

<p align="center">
 <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/2d5294b7-f14b-4d5b-9179-9debcdeb0149" width="18%" style="margin-right: 10px;" />
 <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/265fffb8-e877-4d34-9aa9-4347072263e7" width="18%" style="margin-right: 10px;" />
 <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/5eb55c7f-9cd9-4b6a-b092-d6e042d33c2d" width="18%" style="margin-right: 10px;" />
 <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/1a9b2af8-10df-4b6b-93ad-a7b8bc36e26d" width="18%" style="margin-right: 10px;" />
 <img src="https://github.com/oyunseong/RelaxingSound/assets/42116216/5b864701-72aa-40e1-a69e-cb017e470c0f" width="18%" />
</p>

---

### 💡 프로젝트를 통해 얻은 경험

•	Jetpack Compose 실전 UI 설계 경험

•	MVVM + 상태 관리 구조 이해

•	Room과 Firebase를 함께 사용하는 데이터 구조 설계

•	실제 사용자 사용 흐름을 고려한 UX 기획 경험

•	단순 기능 구현이 아닌 지속 사용 가능한 앱 설계의 중요성 인식

---

### 🙋‍♂️ Developer

•	오해성(Android Developer)

•	오윤성(Android Developer)
