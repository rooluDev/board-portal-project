# 📋 게시판 포탈 사이트

## 📝 프로젝트 개요
이 프로젝트는 게시판 포털 사이트를 구축하는 것을 목표로 합니다. 

게시판의 종류로는 자유 게시판, 문의 게시판, 갤러리 게시판과 공지사항 총 4개의 게시판으로 구성되어 있습니다.

서버를 2개를 두어 사용자 페이지와 관리자 페이지를 나누어 제작했습니다.

사용자 페이지는 SPA(Single Page Application)로 SpringBoot와 Vue.js를 통해 제작했으며. 반면, 관리자 페이지는 MPA(Multi Page Application)로 SpringBoot와 Thymeleaf를 통해 제작했습니다.

## 💻 게시판 구조
+ **관리자 페이지**

  ![스크린샷 2024-07-03 오전 1 48 40](https://github.com/rooluDev/board-portal-project/assets/152958052/de163dde-e054-45a2-ab1a-9c24c66579ad)

+ **사용자 페이지**

  ![스크린샷 2024-07-03 오전 1 44 44](https://github.com/rooluDev/board-portal-project/assets/152958052/3be17fef-4c92-4e07-8611-d81a5cb7541a)


## 🔗 게시판 페이지 링크
+ **[관리자 페이지](http://3.35.111.101:8082/login) (MPA 버전)**
 
> **관리자 아이디:** admin  
> **관리자 비밀번호:** 1234
 
+ **[사용자 페이지](http://3.35.111.101/) (SPA 버전)**

> **사용자 아이디:** user  
> **사용자 비밀번호:** 1234

## 📚 API 문서 [(더 보러가기)](https://documenter.getpostman.com/view/32925626/2sA3JRXyGT)

+ Postman으로 작성한 REST API 문서입니다.


![스크린샷 2024-07-03 오전 12 57 34](https://github.com/rooluDev/board-portal-project/assets/152958052/71e90744-543d-415b-a027-94109042d4da)


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
+ 자유게시판 작성
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
    
    Repository
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

+ 자유 게시판 수정
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
    [Controller 전체코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/controller/FreeBoardController.java#L132-L171)

    [Storage Servie 전체코드](https://github.com/rooluDev/board-portal-project/blob/main/user-page/backend/src/main/java/com/user/backend/service/FileStorageServiceImpl.java#L36-L54)
  </details>

+ 물리적 파일 저장소 변경에 대한 유연성 확보
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

+ 파일 생성 및 수정시 썸네일 삭제 및 생성
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


+ JPA 동적쿼리 작성
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
