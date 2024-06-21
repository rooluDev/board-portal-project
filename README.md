# 📋 게시판 포탈 사이트

## 📝 프로젝트 개요
이 프로젝트는 게시판 포털 사이트를 구축하는 것을 목표로 합니다. 

게시판의 종류로는 자유 게시판, 문의 게시판, 갤러리 게시판과 공지사항 총 4개의 게시판으로 구성되어 있습니다.

사용자 페이지는 SPA로 SpringBoot와 Vue.js를 통해 제작했고 관리자 페이지는 MPA로 SpringBoot와 Thymeleaf로 제작했습니다.


## 🔗 게시판 페이지 링크
+ **[관리자 페이지](http://3.35.111.101:8082/login) (MPA 버전)**
 
> **관리자 아이디:** admin  
> **관리자 비밀번호:** 1234
 
+ **[사용자 페이지](http://3.35.111.101/) (SPA 버전)**

> **사용자 아이디:** user  
> **사용자 비밀번호:** 1234

## 📚 [API 문서 링크](https://documenter.getpostman.com/view/32925626/2sA3JRXyGT)

+ Postman으로 작성한 REST API 문서입니다.

## 📺 화면
+ ## 사용자 페이지
  + **메인 페이지 및 게시판 페이지**
  
    ![사용자 메인 및 게시판](https://github.com/rooluDev/board-portal-project/assets/152958052/2912c0c6-168c-4caf-84e0-39a475a9935b)
  + **자유 게시판 작성**
  
    ![자유 게시판 작성](https://github.com/rooluDev/board-portal-project/assets/152958052/8aa9b76e-705a-41e4-8e95-8223ab60b4cb)
  + **자유 게시판 보기**
  
    ![자유 게시판 보기](https://github.com/rooluDev/board-portal-project/assets/152958052/728748f3-da72-4698-b8a6-0d1aa887db6b)
  + **자유 게시판 수정**
  
    ![자유 게시판 수정](https://github.com/rooluDev/board-portal-project/assets/152958052/99bb67c2-e9f9-45d1-90df-64c944ea087a)
  + **자유 게시판 삭제**
  
    ![자유 게시판 삭제](https://github.com/rooluDev/board-portal-project/assets/152958052/93f822e7-c842-4b49-ab0a-c37375cc0f35)
  + **댓글 등록 및 삭제**
  
    ![댓글 등록 및 삭제](https://github.com/rooluDev/board-portal-project/assets/152958052/f27fcce8-a762-4c8a-8463-de0b89c45a97)

+ ## 관리자 페이지
  + **로그인 및 게시판 페이지**

    ![관리자 로그인 및](https://github.com/rooluDev/board-portal-project/assets/152958052/12419a4a-ef7c-43d2-a193-5c167f3c35f9)
  + **공지사항 작성**
  
    ![공지사항 작성](https://github.com/rooluDev/board-portal-project/assets/152958052/46ee4660-62b4-4725-94ef-101b03fd4fd2)
  + **문의 게시판 보기 및 답변**

    ![문의 게시판 답변](https://github.com/rooluDev/board-portal-project/assets/152958052/eccb71f4-af5d-4dcf-b510-11ab65e39060)  
  + **공지사항 수정 및 삭제**

    ![공지사항 수정 및 삭제](https://github.com/rooluDev/board-portal-project/assets/152958052/c4a1d2ba-f7a4-4ac6-9a87-b1f91062c72e)


## 💡 주요 기능
+ Multipart Form data를 이용한 자유게시판 작성
 <details>
  <summary>코드 보기</summary>
  
Controller
 ```
  @PostMapping("/board/free")
    public ResponseEntity addBoard(@Valid @ModelAttribute FreeBoardDto freeBoardDto,
                                   @RequestPart(name = "file", required = false) MultipartFile[] fileList,
                                   HttpServletRequest request) {

        ...

        return ResponseEntity.ok().build();

 ```
DB Service
```
    /**
     * 자유게시물 추가
     *
     * @param freeBoardDto ( category_id, author_type, author_id, title, content )
     */
    Long addBoard(FreeBoardDto freeBoardDto);
```
Storage Service
```
    /**
     * Multipart File List DB저장 및 물리적 파일 저장
     *
     * @param fileList 저장할 파일 리스트
     * @param boardId 게시판 번호
     * @param boardType 게시판 타입
     * @param thumbnail 썸네일 저장 할지
     */
    void storageFileList(MultipartFile[] fileList, Long boardId, String boardType, boolean thumbnail);
```

Mapper
```
    /**
     * INSERT tb_free_board
     *
     * @param freeBoardDto ( category_id, author_type, author_id, title, content )
     */
    void insertBoard(FreeBoardDto freeBoardDto);
```

[Controller 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/controller/FreeBoardController.java#L99-L130)

[Storage Service 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/service/FileStorageServiceImpl.java#L25-L34)
 </details>

## 🗂 ERD
![ERD](https://github.com/rooluDev/board-portal-project/assets/152958052/a2754673-1a6c-4915-85d6-b30e3e180a89)

+ 댓글과 파일 테이블은 자유 게시판, 갤러리 게시판에 종속적이지만 외래키를 통해 접근을 하게 된다면 댓글과 첨부파일이 있는 게시판이 증설될 경우에 확장성이 높지 않다고 판단하여 boardType(게시판 종류), boardId(게시판 PK)를 구분자로 두어 진행했습니다.

+ 자유 게시판, 갤러리 게시판은 관리자와 사용자가 모두 작성이 가능해 이번 프로젝트의 ERD는 멤버, 관리자 테이블을 따로 두어 위와 같이 authorType(글쓴이 유형), authorId(글쓴이 PK)를 구분자로 두어 진행했습니다.

  
## 🛠 기술 스택
### 🔧 관리자 페이지(MPA)
![Thymeleaf](https://img.shields.io/badge/thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white)

### 🌐 사용자 페이지(SPA)
![Vue.js](https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![Spring Boot](https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MyBatis](https://img.shields.io/badge/MyBatis-000000?style=for-the-badge&logo=MyBatis&logoColor=white)

### 🗄 DB
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### ☁ 인프라
![Amazon EC2](https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
