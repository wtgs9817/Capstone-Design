# 🎓 Hallym Software Graduation Self-Check Web App

한림대학교 정보과학대학 소프트웨어학부 학생들을 위한  
졸업 자가진단 및 정보 커뮤니티 플랫폼입니다.


---
### ⭐ update
- 25/06/16 - Spring Boot 모니터링 시스템 추가(prometheus + grafana)


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
- **팀원**: 김명찬 (빅데이터 17)  백엔드(DB설계, 자가진단 로직), 프로젝트 배포, 모니터링 시스템 구축(프로젝트 종료 후 추가)
  


---

### 👍 개선점
#

**N+1 문제 해결 및 응답속도 42.9% 개선** : 전공 과목 저장 API의 응답 속도를 분석한 결과, 최대 62개의 과목을 저장할 경우 평균 1.9 ~ 2.3초가 측정되었습니다. 사용자 입장에서 지나치게 느리다고 판단했습니다.  이 부분을 개선하기 위해 로그를 분석한 결과, saveAll() 사용 시 내부적으로 각 객체에 대해 SELECT 쿼리가 반복적으로 수행되고 있었으며, 이는 JPA가 해당 객체가 영속상태인지 아니라면 DB에 존재하는 지 확인하기 위해 자동으로 수행하는 동작이라는 것을 알 수 있었습니다. <br />
하지만 이 프로젝트에서는 이미 Set을 사용하여 중복을 필터링하고 있으며, 프론트엔드에서 전달된 과목명이 DB에 존재하는지 체크하는 로직도 별도로 존재하기 때문에 JPA의 영속여부는 불필요한 오버헤드입니다.
이에 따라 saveAll() 대신 Native SQL을 사용하여 구조를 변경하였습니다. <br />  또한, 프론트엔드에서 전달된 과목명 중 DB에 존재하지 않는 값이 있을 경우 RuntimeException을 발생시켜 전체 트랜잭션을 롤백하도록 하여 데이터 정합성도 유지했습니다.  리팩토링 후 응답속도는 1.1 ~ 1.3초로 개선, 약 42.9% 성능 향상을 달성하였습니다. <br /> <br />


**모니터링** : 이번 프로젝트에서는 Spring Boot 애플리케이션과 Nginx 의 상태를 실시간으로 파악하기 위해서 모니터링 시스템(Grafana + Prometheus)을 구축하였습니다. 이를 통해 CPU 사용량, 메모리 사용량, HTTP 요청 수, 상태 코드 등을 수치로 시각화하여 확인할 수 있습니다. <br />
CPU 사용량이나 메모리 사용량이 급격하게 상승하거나 특정 API의 요청이 비정상적으로 집중되는 경우 이를 실시간으로 감지하여 확장 또는 장애 대응이 가능하도록 하였습니다. 예를 들어 비정상적인 HTTP 상태 코드가 반복적으로 기록되는 경우, API 엔드포인트와 요청 수, 요청에 대한 처리 성공 수를 확인하여 원인을 추적하고 문제를 해결할 수 있습니다. 이처럼 모니터링 시스템을 통해 애플리케이션의 상태를 수치 기반으로 인지하고, 문제가 발생하기 전 또는 발생 후 즉시 대응하는 백엔드 운영 역량을 키울 수 있었습니다. <br /><br />



**DB** : 이전 프로젝트에서는 팀장님의 로컬 환경에 구축된 데이터베이스를 사용하여, PC가 종료될 경우 팀 전체의 작업에 제한이 있었습니다. 이러한 문제를 해결하기 위해, 이번 프로젝트에서는 AWS에서 제공하는 관계형 데이터베이스 서비스(RDS)를 사용했습니다. 이를 통해 항상 안정적으로 접근하고 협업이 가능한 환경을 구축할 수 있었습니다.

<br />

**배포 문제** : 이전 프로젝트에서는 WAR 방식으로 배포를 진행하였으며, 이 과정에서의 JDK 버전, 설정 경로, WAS 버전 등 환경 차이로 인한 오류가 발생했었습니다. 또한, 배포 과정 자체도 복잡하고 수동 작업이 많아 효율성이 떨어졌습니다. <br /> 이번 프로젝트에서는 Docker 기반의 배포방식을 사용하여 이러한 문제를 해결하고자 했습니다. 개발 및 테스트가 완료된 환경을 Docker 이미지로 패키징한 뒤, 해당 이미지를 팀원들과 공유하고, 각자의 로컬에서 동일한 이미지 기반의 컨테이너로 실행함으로써 환경 차이에서 발생하던 오류를 해결할 수 있었습니다. Docker 의 경우 WAR 방식과 달리 실행 환경 자체를 이미지로 패키징하기 때문에 애플리케이션을 빌드한 개발자의 환경 그대로를 다른 PC 에서 재현 가능하다는 점에서 안정성과 유지보수성을 개선시킬 수 있었습니다. 이로인해 배포 시간을 단축하고 협업중에 발생하는 환경문제를 해결했습니다.

<br />

**Spring Boot + JSP 조합에서 발생한 문제** : 이번 프로젝트에서는 기존의 Spring Boot + JSP 단일 구조를 사용하여 동적 데이터 처리, 클라이언트 응답, JSP 렌더링을 모두 WAS가 전담하는 방식으로 구현했습니다. 이 구조는 프로젝트 규모가 작을 경우에는 큰 문제가 발생하지 않았지만, 실무 수준의 대규모 트래픽이나 복잡한 비즈니스 로직 환경에서는 WAS에 부하가 생기고, 응답 지연 및 유지보수의 어려움이 발생할 수 있는 구조적인 문제가 있다는 걸 알 수 있었습니다. <br /> 이번 프로젝트에서는 (Nginx + React) + Spring Boot 구조를 사용하여 WAS 는 동적 데이터 처리, 클라이언트 응답에 집중하고 웹서버(Nginx)에서는 React 에서 렌더링된 정적 리소스를 받아서 클라이언트에 보여주고 React 에서 rest api 를 통해 WAS 로 보내는 요청을 받아 WAS 쪽으로 요청을 전송하고 응답을 받아 다시 React 로 보내주는 리버스 프록시 역할을 하며 모든 작업을 전담하던 이전 방식과 달리 역할을 분리하여 서버의 부하를 줄이고 유지보수 측면에서도 실무에서 적용 가능한 구조로 개선되었다고 판단합니다.

<br />




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

---

### 프로젝트 구조도
![캡스톤디자인_최종](https://github.com/user-attachments/assets/d9aeb0c3-83e2-446e-a942-7c10cd712be0)



---

### 모니터링 구조도
![캡스톤디자인_모니터링_아키텍쳐](https://github.com/user-attachments/assets/4578488e-06d5-43ec-8c41-95b9904ed540)




---

### ERD
https://www.erdcloud.com/d/GY9Xrhxdv5uLzPhDA

![image](https://github.com/user-attachments/assets/5a7ae3d4-05db-40f0-9717-511959af14c8)

---
### API 명세서
https://www.notion.so/215093794d9780af93a5c95188562951?v=215093794d978017b619000c8b379007&source=copy_link

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

  











