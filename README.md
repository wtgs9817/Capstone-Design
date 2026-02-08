# 🎓 Hallym Software Graduation Self-Check Web App

한림대학교 정보과학대학 소프트웨어학부 학생들을 위한  
졸업 자가진단 및 정보 커뮤니티 플랫폼입니다.

### 포트폴리오 : https://www.notion.so/2ff093794d9780a3a781d732b26912ff

---
### ⭐ update
- 25/10/19 - nGrinder 추가
- 25/09/18 - Redis 추가
- 25/06/16 - 모니터링 시스템 추가(prometheus + grafana)
  

---


### ⏳ 개발 기간
- 2025년 3월 ~ 2025년 5월 (총 2개월)

---


## 👨‍👩‍👧‍👦 팀 정보

- **작업용 Github 주소** : https://github.com/mins8578/Capstone-Design
- **Notion** : https://www.notion.so/2025-1-1b4877d9e0958069924de4981963c771

- **팀명**: K2C1
- **지도교수**: 신미영 교수님
- **팀장**: 최승혁 (콘텐츠IT 20)  프론트엔드 
- **팀원**: 김민석 (빅데이터 20)  백엔드(로그인, 게시판 로직) 
- **팀원**: 김명찬 (빅데이터 17) 백엔드(DB설계, 자가진단 로직), 프로젝트 배포, [ (모니터링 시스템 구축, Redis 추가), 성능 테스트 → 프로젝트 종료 후 ]
  


---




## 📌 프로젝트 개요

> 기존 학과 홈페이지의 정보 접근성과 커뮤니케이션 한계를 보완하여,  
> 졸업요건·이수과목·학년별 커리큘럼 정보를 손쉽게 조회할 수 있는 졸업 자가진단 기능과,  
> 취업정보 게시판, 공지사항 연동, 학생 간 소통을 위한 커뮤니티 기능을 추가 구현했습니다.  
> 이를 통해 신입생·전과생·편입생도 학사정보에 쉽게 접근하고, 재학생 간 유기적인 소통이 가능한 환경을 제공합니다. 

---

## ⚙️ 핵심 기능

- **🎓 졸업 자가진단**  
  필수 과목 이수 여부, 전공별 커리큘럼 안내, 학년별 추천 교과목

- **📢 학과 공지사항 연동**  
  학과 홈페이지 공지를 실시간 연동

- **💬 선후배 커뮤니티 게시판**  
  자유로운 질의응답 및 경험 공유 공간

- **🖥️ 모니터링 시스템** <br/>
Spring Boot Actuator, Nginx exporter, Node exporter 기반 시계열 메트릭을 prometheus로 수집 <br />
grafana로 시각화하여 정상 범위를 벗어난 이상 징후를 실시간으로 인지하고 문제해결을 할 수 있도록 설계
   
---

## 🧑‍💻 주요 기술 스택

### 🖼️ Frontend
- React
- HTML / CSS / JavaScript
- Figma (와이어프레임 설계)

### 🧠 Backend
- Java
- Spring Boot 
- Spring Data JPA
- Redis 

### 🗄️ Database
- MySQL
- AWS RDS
- ERD 기반 설계

### 🌐 인프라 / 배포
- AWS EC2 (서버 배포)
- Nginx (리버스 프록시 & 정적 리소스 서빙)
- Docker (개발 환경 컨테이너화)

### 🖥️ Monitoring
-  Spring Boot Actuator (애플리케이션 레벨 메트릭 수집)
-  nginx exporter (Nginx 웹 서버 상태 정보 수집)
-  node exporter (서버의 자원 메트릭 수집)
-  prometheus  (시계열 메트릭 수집 및 저장)
-  grafana  (수집한 메트릭 시각화)

### 🧰 기타 도구
- GitHub (협업 및 버전 관리)
- Notion (프로젝트 진행 일지)
- VS Code / IntelliJ (개발 환경)
- nGrinder (성능 테스트)

---

### 프로젝트 구조도
<img width="1001" height="617" alt="캡스톤디자인_진짜진짜로최종 drawio" src="https://github.com/user-attachments/assets/5ef75120-5b52-4674-85c1-7791172d533b" />






---

### 모니터링 구조도
![캡스톤디자인_모니터링_아키텍쳐](https://github.com/user-attachments/assets/4578488e-06d5-43ec-8c41-95b9904ed540)




---

### ERD
https://www.erdcloud.com/d/GY9Xrhxdv5uLzPhDA

![image](https://github.com/user-attachments/assets/5a7ae3d4-05db-40f0-9717-511959af14c8)

---
### API 명세서
[https://www.notion.so/215093794d9780af93a5c95188562951?v=215093794d978017b619000c8b379007&source=copy_link](https://soapy-tilapia-835.notion.site/215093794d9780af93a5c95188562951?v=215093794d978017b619000c8b379007)

![image](https://github.com/user-attachments/assets/f8991569-103a-43b9-be07-699efe374029)





---

### 👤 회원가입
<p align="center">
  <img src="https://github.com/user-attachments/assets/106a8ace-4749-4347-ac80-2114ac148881" width="90%" />
</p>

- 학교 이메일 기반 인증 코드 발송 및 검증 후 회원가입 진행한다. (학교 학생 외 가입 차단)

<br />
<img width="519" height="34" alt="image" src="https://github.com/user-attachments/assets/6ecfe7c2-005b-4e61-bd7b-f304b8a19e2a" />

- 사용자 비밀번호를 BCrypt 해시 알고리즘으로 암호화하여 보안성 강화(Spring Security의 BCryptPasswordEncoder 사용)
- 해시값은 복호화가 불가능한 단방향 암호화 방식이며, 로그인 시 입력값을 동일한 방식으로 해싱한 뒤 저장된 해시값과 비교하여 인증을 수행한다.

---


### 🎉 메인화면

<p align="center">
  <img src="https://github.com/user-attachments/assets/ccafdba9-b69c-4873-95c9-85418b0e927b" width="90%" />
</p>

- 사용자는 홈페이지 메인 화면에서 프로젝트 전반의 정보를 확인할 수 있다.

---

### 🧑‍💻 로그인

<p align="center">
  <img src="https://github.com/user-attachments/assets/1fe8d737-c4b2-48eb-923b-45f750b506a3" width="45%" />
</p>

- 사용자는 학교 이메일을 통해 본인 인증 후 회원가입을 진행한다.  
- 가입 완료 후 로그인하여 서비스를 이용할 수 있다.
- 로그인 시 JWT(Json Web Token) 이 생성되고, 클라이언트 localstorage 에 저장 (토큰 유효기간 1시간)
- 토큰의 서명, 만료 시간, 사용자 ID 검증하여 토큰의 유효성을 판단  

---

### 📘 졸업 자가진단 페이지

<p align="center">
  <img src="https://github.com/user-attachments/assets/b247929d-45a5-48d6-b136-6027ceda93ef" width="90%" />
</p>

- "현재 이수 과목 등록" 버튼을 눌러 이수한 과목을 체크한 뒤 저장할 수 있다.
-  과목 정보를 수정할 경우, 선택된 과목을 해제한 후 다시 저장하면 반영이 된다.
-  저장된 과목 정보를 바탕으로 졸업 자가진단 결과를 확인할 수 있다.


<p align="center">
  <img src="https://github.com/user-attachments/assets/0bfa3662-6796-495f-9c1b-3f2ebc757f28" width="90%" />
</p>

- 우측 트랙 배너를 클릭하면 전공별 트랙 및 학년별 추천 과목을 확인할 수 있다.

---

### 🗣️ 커뮤니티 페이지

<p align="center">
  <img src="https://github.com/user-attachments/assets/4d7f0224-eedd-4274-b52e-ecf2403c13da" width="45%" />
  <img src="https://github.com/user-attachments/assets/665b7f34-f374-445c-b52b-b2e2416d7d27" width="45%" />
</p>

- 사용자는 글쓰기 버튼을 눌러 게시글을 작성하고 등록할 수 있다.

<p align="center">
  <img src="https://github.com/user-attachments/assets/c0c4d14f-ddce-40a4-afd8-03534656ef33" width="90%" />
</p>

- 작성한 게시글은 본인이 수정하거나 삭제할 수 있다.  
- 게시글 내에서 공감(좋아요) 버튼을 누를 수 있으며, 댓글 작성 후 본인의 댓글에 한해 수정/삭제가 가능하다.

---


### 🖥️ 모니터링 화면
**NGINX**
![image](https://github.com/user-attachments/assets/8ed8ee1a-2d6b-47c7-a27f-555489455860)



**Spring Boot**
![image](https://github.com/user-attachments/assets/d0547b47-c53e-4c9f-a75d-f296468d1c1f)
 
- CPU 사용률, 네트워크 트래픽, HTTP 요청 응답 상태 등의 시계열 메트릭을 실시간으로 시각화하고 모니터링함으로써, 비정상적인 수치나 연결 요청 실패 등 이상징후를 조기에 탐지하고 빠르게 대처할 수 있도록 구성

  











