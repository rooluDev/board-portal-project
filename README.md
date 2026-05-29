# 📋 게시판 포탈 사이트

## 📝 프로젝트 개요
이 프로젝트는 **다양한 게시판을 통합적으로 관리할 수 있는 포털 사이트**를 구축하는 것을 목표로 합니다.

- **게시판 종류**: 자유 게시판, 문의 게시판, 갤러리 게시판, 공지사항
- **아키텍처 구성**:
  - **사용자 페이지**: SPA(Single Page Application) 구조, **Spring Boot + Vue.js** 기반
  - **관리자 페이지**: MPA(Multi Page Application) 구조, **Spring Boot + Thymeleaf** 기반
- **서버 분리**: 사용자 페이지와 관리자 페이지를 각각 다른 서버에서 운영하여 보안성과 관리 효율성을 강화했습니다.

## 💻 게시판 구조
+ **관리자 페이지**

  ![스크린샷 2024-07-03 오전 1 48 40](https://github.com/rooluDev/board-portal-project/assets/152958052/de163dde-e054-45a2-ab1a-9c24c66579ad)

+ **사용자 페이지**

  ![스크린샷 2024-07-03 오전 1 44 44](https://github.com/rooluDev/board-portal-project/assets/152958052/3be17fef-4c92-4e07-8611-d81a5cb7541a)


## 🔗 게시판 페이지 링크
+ **[관리자 페이지](https://admin.김승현포털사이트.site/login) (MPA 버전)**
 
> **관리자 아이디:** admin  
> **관리자 비밀번호:** admin
 
+ **[사용자 페이지](https://김승현포털사이트.site) (SPA 버전)**

> **사용자 아이디:** user  
> **사용자 비밀번호:** 1234


## 📺 화면
  + **메인 페이지 및 로그인**
  

https://github.com/rooluDev/board-portal-project/assets/152958052/a7594704-185f-46af-a8ab-c3975048afe6

  + **게시물 검색**


https://github.com/rooluDev/board-portal-project/assets/152958052/d1e4bd23-f7bd-4f99-948e-d6230cfc6082


  + **게시판 작성**
  

https://github.com/rooluDev/board-portal-project/assets/152958052/d6bdab38-46f8-4d8b-aa4b-09945b361388


  + **게시판 수정**

https://github.com/rooluDev/board-portal-project/assets/152958052/a27fb564-752d-4474-ae85-1f373fdb7843


  
  + **댓글 등록**


https://github.com/rooluDev/board-portal-project/assets/152958052/8b9cae92-1f60-4d93-84f9-7eb3df5dc3bf


  + **파일 다운로드**
  

https://github.com/rooluDev/board-portal-project/assets/152958052/513d4a70-ab98-4436-8060-178c9e7edbed



## 💡 주요 기능

### 1️⃣ 자유 게시판 작성

자유 게시판에서는 텍스트 게시글뿐만 아니라 첨부파일을 포함한 게시글을 작성할 수 있습니다.  
클라이언트에서 `multipart/form-data` 형식으로 게시글 정보와 첨부파일을 함께 업로드하면,  
서버에서 이를 파싱하여 **DB 저장(게시글 메타데이터)**과 **물리적 파일 저장(파일 서버)**을 분리하여 처리합니다.  
이 과정을 통해 파일 업로드와 게시글 작성이 하나의 트랜잭션처럼 동작하도록 구성했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
  
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
    
    [Controller 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/controller/FreeBoardController.java#L114-L145)
    
    [Storage Service 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/service/LocalStorageService.java#L34-L68)
  </details>

### 2️⃣ 자유 게시판 수정

자유 게시판 수정 기능은 기존 게시물의 제목과 내용을 수정하고, 첨부파일을 추가하거나 기존 파일을 삭제할 수 있도록 구현되었습니다.  
수정 요청 시 **게시글 데이터 업데이트, 파일 삭제, 새 파일 업로드**가 순차적으로 진행되며,  
썸네일이 필요한 경우 자동으로 재생성되도록 설계되었습니다.  
이를 통해 게시물과 첨부파일이 항상 최신 상태로 유지되도록 구성했습니다.


  <details>
    <summary>코드 보기(펼치기/접기)</summary>
   
     Controller
     ```
      /**
        * 자유게시판 수정
        *
        * @param boardId          PathVariable ( pk )
        * @param freeBoardDto     수정할 데이터
        * @param fileList         추가할 파일
        * @param deleteFileIdList 삭제할 파일의 pk 리스트
        * @param request          HttpServletRequest
        * @return null
        */
       @PutMapping("/board/free/{boardId}")
       public ResponseEntity modifyBoard(@PathVariable(name = "boardId") Long boardId,
                                         @Valid @ModelAttribute FreeBoardDto freeBoardDto,
                                         @RequestParam(name = "deleteFileIdList") List<Long> deleteFileIdList,
                                         @RequestPart(name = "file", required = false) MultipartFile[] fileList,
                                         HttpServletRequest request) {
   
           
           ...
  
           return ResponseEntity.ok().build();
      ```
      
      DB Service
      ```
      /**
       * 게시물 수정
       *
       * @param freeBoardDto ( categoryId, title, content, boardId )
       */
      void modifyBoard(FreeBoardDto freeBoardDto);
      ```
      
      Storage Service
      ```
      /**
       * 썸네일 DB저장 및 물리적 파일 저장
       *
       * @param fileDto 썸네일로 저장할 FileDto
       */
      void storageThumbnail(FileDto fileDto);
  
      /**
       * 파일 리스트 삭제
       *
       * @param deleteFileIdList 삭제할 파일들의 pk 리스트
       * @return 썸네일로 만든 파일 대상이 삭제가 되었는지
       */
      boolean deleteFileList(List<Long> deleteFileIdList);
      ```
      Mapper
      ```
      /**
       * UPDATE tb_free_board
       *
       * @param freeBoardDto ( categoryId, title, content, boardId )
       */
      void updateBoard(FreeBoardDto freeBoardDto);
      ```
      [Controller 전체코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/controller/FreeBoardController.java#L147-L186)
  
      [Storage Servie 전체코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/service/LocalStorageService.java#L34-L68)
   </details>

### 3️⃣ 파일 저장소 유연성 확보

이 기능은 **파일의 물리적 저장 위치(local storage, NAS, cloud storage 등)**가 변경되더라도  
애플리케이션 로직을 수정하지 않고 쉽게 대응할 수 있도록 설계되었습니다.  
이를 위해 **파일의 메타데이터(DB 저장)와 실제 물리적 파일 저장 로직을 분리**하였으며,  
`FileStorageService`가 이 두 서비스를 주입받아 통합적으로 처리하도록 구성했습니다.  
이 구조를 통해 파일 저장소의 변경이 필요한 경우에도 StorageService 구현체만 교체하면 되어,  
유지보수성과 확장성이 크게 향상되었습니다.

  <details>
    <summary>코드 보기(펼치기/접기)</summary>

     물리적 파일의 저장 위치 변경에 대응하기 위하여 (local storage, cloud storage, NAS 등...) 물리적 파일을 저장하는 StorageService Interface와 metadata를 저장하는 FileService Interface를 분리하고 
     위 두 인터페이스를 의존성을 주입하여 작동하는 FileStorageService를 작성해 유연성을 확보하였다.
   
          Metadata 저장소
          ```
         /**
         * File Service Interface
         */
         public interface FileService {
         
             /**
              * File 등록
              *
              * @param fileList DB에 저장할 File List
              * @param boardId  boardId ( pk )
              * @return 저장된 FileList
              */
             List<FileDto> addFileList(List<FileDto> fileList, Long boardId);
      
             ...
          ```
      
          물리적 파일 저장소
          ```
         /**
          * Storage Service
          */
         public interface StorageService {
         
             /**
              * Multipart File 리스트 물리적 파일 생성
              *
              * @param multipartFiles 저장할 파일
              * @param boardType 보드 타입
              * @return 저장된 파일들 FileDto 리스트
              */
             List<FileDto> storageFileList(MultipartFile[] multipartFiles, String boardType);
         
             /**
              * FileDto로 썸네일 물리적 생성
              *
              * @param fileDto 생성할 원본 파일
              * @return 생성된 Thumbnail의 객체
              */
             ThumbnailDto storageThumbnailFromFile(FileDto fileDto);
         }
         ```
      
         FileStorageService impl
         ```
         /**
          * FileStorageService Impl
          */
         @Service
         @RequiredArgsConstructor
         @Primary
         public class FileStorageServiceImpl implements FileStorageService {
         
             private final StorageService storageService;
             private final FileService fileService;
             private final ThumbnailService thumbnailService;
      
         ...
         ```
  
      [FileStorage Service 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/service/FileStorageServiceImpl.java)
  
   </details>

### 4️⃣ 파일 생성 및 수정시 썸네일 삭제 및 생성

이 기능은 **썸네일이 필요한 게시판(갤러리)**과 **썸네일이 필요 없는 게시판(자유 게시판)**을 모두 지원하기 위해 설계되었습니다.  
파일을 생성하거나 수정할 때 **썸네일 생성 여부를 메소드 파라미터로 제어**할 수 있도록 구성하여,  
하나의 공통 `FileStorageService` 메소드로 두 유형의 게시판을 모두 지원할 수 있습니다.  

또한 **파일 삭제 시 해당 파일이 썸네일 대상이었는지를 판단하여 썸네일도 함께 제거**하도록 처리하여,  
불필요한 썸네일 파일이 서버에 남지 않도록 했습니다.

  <details>
     <summary>코드 보기(펼치기/접기)</summary>
     썸네일이 필요한 게시판(갤러리)과 썸네일이 필요 없는 게시판(자유 게시판) 둘 다 사용하는 File Storage Service에서 썸네일의 생성 유무를 직접 주입해서 둘 다 사용 가능한 메소드를 생성했다.
  
     삭제 메소드
     ```
     /**
       * 파일 리스트 삭제
       *
       * @param deleteFileIdList 삭제할 파일들의 pk 리스트
       * @return 썸네일로 만든 파일 대상이 삭제가 되었는지
       */
      boolean deleteFileList(List<Long> deleteFileIdList);
     ```
     
     생성 메소드
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
  
     [FileStorageServiceImpl 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/service/FileStorageServiceImpl.java)
  </details>


### 5️⃣ JPA 동적쿼리 작성

이 기능은 **검색 조건이 다양하게 조합될 수 있는 게시판 검색 기능**을 효율적으로 처리하기 위해 구현되었습니다.  
JPA의 **Specification**과 **CriteriaBuilder**를 활용하여 조건별로 `Predicate`를 동적으로 생성하고,  
검색 조건이 없을 경우 해당 조건을 무시하도록 하여 **불필요한 where 절 생성을 방지**했습니다.

  <details>
   <summary>코드 보기(펼치기/접기)</summary>
     JPA의 Specification과 CriteriaBuilder를 활용하여 조건별 Predicate를 동적으로 생성하고, 검색 조건이 없는 경우에는 해당 조건을 무시하도록 구현.
  
     자유 게시판 Specification Class
     ```
     /**
       * 검색조건을 통한 쿼리 생성
       *
       * @param searchConditionDto 검색 조건
       * @return 쿼리
       */
      public static Specification<FreeBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {
       ...
     ```
     
     FreeBoardRepository
     ```
     default Page<FreeBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {
          Specification<FreeBoard> specification = FreeBoardSpecification.findBySearchCondition(searchConditionDto);
          Sort.Direction direction = Sort.Direction.fromString(searchConditionDto.getOrderDirection());
          String orderValue = searchConditionDto.getOrderValue() != null ? searchConditionDto.getOrderValue() : "createdAt";
          Pageable pageable = PageRequest.of(searchConditionDto.getPageNum() - 1, searchConditionDto.getPageSize(), direction, orderValue);
          return findAll(specification, pageable);
      }
     ```
  
     [FreeBoardSpecification 전체 코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/specification/FreeBoardSpecification.java)
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
![JPA](https://img.shields.io/badge/JPA-000000?style=for-the-badge&logoColor=white)

### 🗄 DB
![MySQL](https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white)

### ☁ 인프라
![Amazon EC2](https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white)
![Nginx](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
