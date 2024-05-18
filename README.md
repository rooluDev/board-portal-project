# 📋 게시판 포탈 사이트

## 📝 프로젝트 개요
웹 개발자로서 필요한 기본 소양을 키우고 또 잘 보여줄 수 있는 게시판 포털 사이트 프로젝트를 진행했습니다.

관리자 페이지는 Spring Boot와 Thymeleaf를 사용해 MPA로, 사용자 페이지는 Spring Boot와 Vue를 통해 SPA로 진행했습니다.

게시판의 종류로는 문의 게시판, 자유 게시판, 갤러리 게시판, 공지사항 4가지로 진행했습니다.

## 🛠 기술 스택
### 🔧 관리자 페이지(MPA)
  * **Front-end**
    * ![Thymeleaf](https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

  * **Back-end**
    * ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
    * ![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white)

### 🌐 사용자 페이지(SPA)
  * **Front-end**
    * ![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
    * ![Vuetify](https://img.shields.io/badge/vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=white)

  * **Back-end**
    * ![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
    * ![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white)

### 🗄 DB
  * ![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### ☁ 인프라
  * ![Amazon EC2](https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
  * ![Nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)

## 🔗 게시판 페이지 링크
+ **[관리자 페이지](http://3.35.111.101:8080/login) (MPA 버전)**
 
> **관리자 아이디:** admin  
> **관리자 비밀번호:** 1234
 
+ **사용자 페이지 (SPA 버전)**

> **사용자 아이디:** user  
> **사용자 비밀번호:** 1234

## 📚 [API 문서 링크](https://documenter.getpostman.com/view/32925626/2sA3JRXyGT)

+ Postman으로 작성한 REST API 문서입니다.

## 📺 화면
+ **사용자 페이지**

https://github.com/rooluDev/board-portal-project/assets/152958052/cb1a80b6-286a-4ac9-a44d-ff9b87691eec

+ **관리자 페이지**

https://github.com/rooluDev/board-portal-project/assets/152958052/cd23df82-18aa-4d47-9845-08079bce5460




## 💡 주요 기능
+ **JWT을 통한 인증 및 인가**  
  [코드 보러가기](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/jwt/JwtProvider.java#L22)

+ **Multipart Form-Data를 통한 게시물 작성 및 수정**  
  [코드 보러가기](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/controller/GalleryBoardController.java#L143)

+ **GlobalExceptionHandler를 통해 Custom ErrorCode와 Exception을 만들어서 에러 핸들링**
  * Backend Handler  
    [코드 보러가기](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/common/exception/handler/GlobalExceptionHandler.java)
  * Frontend Handler  
    [코드 보러가기](https://github.com/rooluDev/board-portal-project/blob/main/user-page/frontend/src/error/index.js)

## 🗂 ERD
![ERD](https://github.com/rooluDev/board-portal-project/assets/152958052/a2754673-1a6c-4915-85d6-b30e3e180a89)

+ 댓글과 파일 테이블은 자유 게시판, 갤러리 게시판에 종속적이지만 외래키를 통해 접근을 하게 된다면 댓글과 첨부파일이 있는 게시판이 증설될 경우에 확장성이 높지 않다고 판단하여 boardType(게시판 종류), boardId(게시판 PK)를 구분자로 두어 진행했습니다.

+ 자유 게시판, 갤러리 게시판은 관리자와 사용자가 모두 작성이 가능해 이번 프로젝트의 ERD는 멤버, 관리자 테이블을 따로 두어 위와 같이 authorType(글쓴이 유형), authorId(글쓴이 PK)를 구분자로 두어 진행했습니다.
