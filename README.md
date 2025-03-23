# 전시로그 - 전시를 기록하다: 전시회 정보 플랫폼

<p align="center">
  <br>
  <img src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/JeonsiLog_Asset_1080.jpg">
  <br>
</p>

<br>

## 📌 [목차](#index) <a name = "index"></a>

- [프로젝트 소개](#intro_project)
- [PlayStore](#play_store)
- [기술 스택](#teck_stack)
- [아키텍처](#architecture)
- [ERD](#erd)
- [API Docs](#api)
- [왜 이 기술을 사용했는가?](#why)
- [ScreenShot](#outputs)

<br>

## 📝 프로젝트 소개 <a name = "intro_project"></a>

### 💡 전시회 정보 플랫폼 <전시로그>
<p>전시로그를 통해 각 전시회의 세부 정보를 확인할 수 있고 별점과 감상평을 통해 자신의 생각과 경험을 나눌 수 있어요</p>
<p>전시로그를 통해 전시회 경험을 쌓아보세요!</p>

<br />

## PlayStore <a name = "plae_store"></a>
[전시로그 - 전시를 기록하다: 전시회 정보 플랫폼](https://play.google.com/store/apps/details?id=com.andletgo.jeonsilog&hl=ko-KR)

<br>

## ⚙️ 기술 스택 <a name = "teck_stack"></a>

<img src="https://github.com/user-attachments/assets/f93e6e01-c976-4e03-8e69-9e2c0e62b493" alt="메인" width="650" />

<br />

## 🧬 아키텍처 <a name = "architecture"></a>

<img src="https://github.com/user-attachments/assets/571ee2ca-b569-49a7-a6a2-50d04400cb87" alt="메인" width="650" />

- Nginx를 리버스 프록시로 사용
- 배포를 위한 jar 파일 및 이미지 저장을 위한 AWS S3 저장소
- Github Actions와 AWS Code Deploy를 사용한 빌드, 테스트 및 배포 자동화 구축

<br />

## 📈 ERD <a name = "erd"></a>

<img src="https://github.com/user-attachments/assets/61512064-4505-4607-b6cf-f4e7a4ec18d8" alt="메인" width="650" />

<br />

## 📑 API Docs <a name = "api"></a>

팀 내 API 문서 공유를 위해 **Swagger**를 적용했습니다.
아래는 문서 내용 일부로, 전체 문서는 [여기]()에서 확인하실 수 있습니다.

<img src="https://github.com/user-attachments/assets/77839959-277b-4661-90bb-4580c6c28846" alt="메인" width="650" />

<br />

## 💎 기술 사용 이유 <a name = "why"></a>

### API 문서화

<img src="https://velog.velcdn.com/images/phonil/post/bac2dced-6435-461e-9e64-5a17651b922a/image.png" alt="메인" width="370" />

**Notion vs Spring REST docs vs Swagger**

일정 관리부터 팀 내 정보 공유와 같은 대부분의 기록을 Notion으로 하고 있었기에 프로젝트 초기에는 Notion을 사용하여 API 문서화를 진행하는 것이 타 파트와의 소통 면에서 가장 효과적일 것이라고 생각했습니다.
하지만, 손수 작성해야 한다는 번거로움과 MVP 개발의 짧은 기간 때문에 Notion은 후보에서 제외하게 되었습니다.

코드 상에서 문서화 할 수 있도록 Swagger를 적용하여 개발을 하였으나, Swagger 코드가 섞여 코드의 가독성이 떨어진다는 불편함이 발생하여 Spring REST docs의 도입을 고민하게 되었습니다.

코드에 문서화를 위한 내용이 섞이지 않는 점과 높은 자유도가 강력하게 느껴졌습니다. 하지만, 짧은 기간에 빠르게 개발해야 하는 상황에서 테스트 코드가 강제된다는 점이 도입에 어려움을 주었고, Swagger를 사용하여 API 문서화를 이어가도록 결정했습니다.

<br>

### Nginx

<img src="https://velog.velcdn.com/images/phonil/post/90d7f411-abe0-4619-b039-c2b1579903f9/image.png" alt="메인" width="300" />

HTTPS와 리버스 프록시 설정을 위해 Nginx를 사용했습니다.
MVP 개발 시점에서는 서버가 큰 상황이 아니지만, 무중단 배포와 정적 이미지 캐싱, 압축을 비롯하여 확장성 면에서 가능성을 열어두고자 Nginx를 도입했습니다.

<br>

### Github Actions

![](https://velog.velcdn.com/images/phonil/post/0cf2ddd3-0e8c-4c65-86b1-b96707130214/image.png)

프로젝트를 진행하며 빠른 수정과 반영을 위해 빌드 및 배포 자동화가 필요했습니다.
Jenkins와 Github Actions라는 두 후보가 있었는데, 아래와 같은 이유로 **Github Actions를 선택**했습니다.

**젠킨스**는 아이템 별 스크립트의 진행 상황을 별도 대시보드에서 확인할 수 있다는 점이 좋았습니다. 하지만, 메모리 소모가 큰 편이라 구축 시 스프링 서버와 별도로 추가적인 서버를 위한 비용이 필요하다는 점과 초기 구축에 번거로움이 있다는 점으로 인해 보류하게 되었습니다. 더욱이 젠킨스의 큰 장점인 여러 플러그인을 통한 호환성과 복잡한 경우에서의 커스터마이징이 해당 프로젝트에는 필요성이 떨어졌습니다.

**GitHub Actions**는 깃허브를 사용하고 있고, 깃허브에서 바로 확인할 수 있다는 점이 좋았습니다. 프로젝트 경험이 적은 팀원과 협업 시에도 한 눈에 확인할 수 있었고, 설정이 간편해 프로젝트 초기부터 빠르게 적용할 수 있었습니다.
복잡한 상황에서의 커스터마이징이 필요하지 않았고, 추가적인 비용이 필요하지 않다는 점이 팀의 기술 선택에 가장 큰 이유입니다.

<br />

## 🎁 ScreenShot <a name = "outputs"></a>
| 스플래시 | 로그인 | 약관동의 | 회원가입 |
| --- | --- | --- | --- |
| <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/splash.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/login.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/tos.jpg" > |<img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/signup.jpg"> |

| 홈 | 상세정보 | 포스터 다운로드 | 감상평 작성 | 댓글 작성 | 전시장 |
| --- | --- | --- | --- | --- | --- |
| <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/home.jpg" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/exhibition_info.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/poster_download.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/review_page.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/reply.png" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/place.png"> |

| 검색 | 검색 결과 | 포토캘린더 | 포스터 불러오기 | 관리자 페이지 | 신고 |
| --- | --- | --- | --- | --- | --- |
| <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/search.jpg" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/search_result.jpg" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/calendar.jpg" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/calendar_poster.jpg" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/admin.png" > | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/report.png" > |

| 내 별점 | 내 감상평 | 내 즐겨찾기 | 설정 |
| --- | --- | --- | --- |
| <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/mypage_rating.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/mypage_review.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/mypage_interest.jpg"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/setting.jpg"> |

| 타 유저 포토캘린더 | 팔로잉 & 팔로워 | 활동 알림 | 전시 리마인더 알림 |
| --- | --- | --- | --- |
| <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/otheruser_calendar.png"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/following.png"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/noti_activity.png"> | <img width="150" src="https://github.com/AndLetgo/2nd-Main-JeonsiLog-Client/blob/readme%231/assets/noti_exhibition.png"> |

