# Fairer
<img width="80" alt="스크린샷 2022-06-29 오전 12 32 29" src="https://user-images.githubusercontent.com/27774564/176220195-0d1bf1da-8e56-4142-88fc-0fc375b8b768.png">

디프만. 11기에서 활동한 도와조 홈즈 팀의 **Fairer**입니다.

[![Facebook](https://img.shields.io/badge/facebook-1877f2?style=flat-square&logo=facebook&logoColor=white&link=https://www.facebook.com/fairer.official/)](https://www.facebook.com/fairer.official)
[![instagram](https://img.shields.io/badge/instagram-E4405F?style=flat-square&logo=Instagram&logoColor=white&link=https://www.instagram.com/fairer.official/)](https://www.instagram.com/fairer.official/)

# OverView
> Peacemaker for houseworker 모든 하우스워커를 위한 피스메이커, 페어러
> 집안일 분담 서비스

- 개발 기간 : 2022.03.19 ~ ing 
- Android 개발자 : <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=Android&logoColor=white"> <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=Kotlin&logoColor=white">

|<img width="200" height="200" src="https://user-images.githubusercontent.com/85485290/176230637-57e8041f-4a38-4ea3-a6f4-970f7bbf02c2.jpeg"/>|<img width="200" height="200" src="https://user-images.githubusercontent.com/85485290/176229713-86711288-ac57-4dd5-bf5d-f2ffc4889303.png"/>|<img width="200" height="200" src="https://user-images.githubusercontent.com/85485290/176229740-b23830e0-7bba-4966-bca4-c570a971d4d7.jpg"/>|<img width="200" height="200" src="https://user-images.githubusercontent.com/50831854/210321806-8ea1cf79-c758-44c4-900c-7138d1407c78.jpg"/>|
|:------:|:---:|:---:|:---:|
|[김수연](https://github.com/onemask)|[임수진](https://github.com/sujin-kk)|[박정준](https://github.com/wjdwns) | [김민주](https://github.com/kimchuu) |


# About

### Features
- 0. 구글 로그인 
- 1. 집안일 생성 
- 2. 집안일 조회
- 3. 집안일 삭제  
- 4. 그룹 생성 , 카카오톡 초대 , 딥링크  
- 5. 프로필 수정 
- 6. 집안일 규칙 추가
- 7. 푸쉬 알림
- 8. 네트워크 연결 감지

### Technology Stack
- Tools : Android Studio Dolphin
- Language : Kotlin
- Architecture Pattern : MVVM Pattern
- Android Architecture Components(AAC)
  - Flow
  - ViewModel
  - Coroutine
  - Data Binding
  - Hilt
- Naivgation Conponponent
- FirebaseMessagingService
- OKHTTP
- RETROFIT
- MOSHI
- SERIALIZATION
- KAKAO_SDK
- GLIDE
- FIREBASK_BOM
- TIMBER 

### Foldering
```
.
├── base
├── data
│   ├── dataSource
│   ├── repository
│   └── ApiService
├── di
│   ├── NetworkModule
│   └── RepositoryModule
├── model
│   ├── enums
│   ├── request
│   └── response
├── service
│   ├── FairerFirebaseMessagingService
│   └── InternetService
├── ui (for features)
└── util
```

# WireFrame
<img width="400" alt="스크린샷 2022-06-29 오전 12 58 34" src="https://user-images.githubusercontent.com/27774564/176225988-3c2a3b19-53a0-4627-89c8-1e808e2ec43b.png">
<img width="400" alt="스크린샷 2022-06-29 오전 12 58 42" src="https://user-images.githubusercontent.com/27774564/176226010-fd300d9c-30dd-4da1-b278-7354ecffb6e4.png">
<img width="400" alt="스크린샷 2022-06-29 오전 12 59 24" src="https://user-images.githubusercontent.com/27774564/176226122-5eb5603a-cfec-40e4-bed2-cd2d0c2167a4.png">
<img width="400" alt="스크린샷 2022-06-29 오전 12 59 38" src="https://user-images.githubusercontent.com/27774564/176226168-12852d78-4506-4f09-9ef2-830a3ade38c4.png">


# ToDo
- 메인 화면 개편
- 룰렛 기능
- 통계 기능

