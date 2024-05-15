package com.user.backend.controller;

import com.user.backend.common.exception.custom.BoardNotFoundException;
import com.user.backend.common.exception.custom.IllegalFileDataException;
import com.user.backend.common.exception.custom.NotLoggedInException;
import com.user.backend.common.exception.custom.NotMyBoardException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.type.Author;
import com.user.backend.common.type.Board;
import com.user.backend.common.utils.PaginationUtils;
import com.user.backend.common.validator.MultipartFileValidator;
import com.user.backend.common.validator.constraint.GalleryBoardFileConstraint;
import com.user.backend.dto.*;
import com.user.backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gallery Board Controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GalleryBoardController {

    private final GalleryBoardService galleryBoardService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final JwtService jwtService;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;
    private final MultipartFileValidator<GalleryBoardFileConstraint> multipartFileValidator;


    /**
     * 갤러리게시판 리스트 페이지에 필요한 데이터 반환
     *
     * @param searchConditionDto 검색조건
     * @return {
     * galleryBoardList:[],
     * categoryList:[],
     * searchCondition:{},
     * totalPageNum:0
     * }
     */
    @GetMapping("/boards/gallery")
    public ResponseEntity<Map> getBoardList(@Valid @ModelAttribute SearchConditionDto searchConditionDto) {

        List<GalleryBoardDto> galleryBoardDtoList = galleryBoardService.getBoardListByCondition(searchConditionDto);
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.GALLERY_BOARD.getBoardType());

        // 페이지네이션 세팅
        int totalRowCount = galleryBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        Map response = new HashMap();
        response.put("galleryBoardList", galleryBoardDtoList);
        response.put("categoryList", categoryDtoList);
        response.put("totalPageNum", totalPageNum);
        response.put("searchCondition", searchConditionDto);

        return ResponseEntity.ok(response);
    }

    /**
     * 갤러리게시판 보기 페이지 및 수정 페이지에 필요한 데이터 반환
     *
     * @param boardId PathVariable ( pk )
     * @return {
     * galleryBoard: {},
     * commentList:[],
     * fileList:[],
     * categoryList:[]
     * }
     */
    @GetMapping("/board/gallery/{boardId}")
    public ResponseEntity<Map> getBoard(@PathVariable(name = "boardId") Long boardId) {

        GalleryBoardDto galleryBoardDto = galleryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        List<FileDto> fileDtoList = fileService.getFileListByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());
        List<CommentDto> commentDtoList = commentService.getCommentListByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.GALLERY_BOARD.getBoardType());

        Map response = new HashMap();
        response.put("galleryBoard", galleryBoardDto);
        response.put("fileList", fileDtoList);
        response.put("commentList", commentDtoList);
        response.put("categoryList", categoryDtoList);

        return ResponseEntity.ok(response);
    }

    /**
     * 게시판 추가
     *
     * @param galleryBoardDto 추가할 데이터
     * @param fileList        추가할 첨부파일
     * @param request         HttpServletRequest
     * @return null
     */
    @PostMapping("/board/gallery")
    public ResponseEntity addBoard(@Valid @ModelAttribute GalleryBoardDto galleryBoardDto,
                                   @RequestParam(name = "file") MultipartFile[] fileList,
                                   HttpServletRequest request) {

        String memberId = jwtService.getMemberIdFromToken(request);

        multipartFileValidator.validateFile(fileList);

        galleryBoardDto.setAuthorId(memberId);
        galleryBoardDto.setAuthorType(Author.MEMBER.getAuthorType());

        Long boardId = galleryBoardService.addBoard(galleryBoardDto);

        if (fileList != null) {
            fileStorageService.storageFileList(fileList, boardId, Board.GALLERY_BOARD.getBoardType(), true);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 게시판 수정
     *
     * @param boardId          PathVariable ( pk )
     * @param galleryBoardDto  수정할 데이터
     * @param fileList         수정할 파일
     * @param deleteFileIdList 삭제할 파일
     * @param request          HttpServletRequest
     * @return null
     */
    @PutMapping("/board/gallery/{boardId}")
    public ResponseEntity modifyBoard(@PathVariable(name = "boardId") Long boardId,
                                      @Valid @ModelAttribute GalleryBoardDto galleryBoardDto,
                                      @RequestParam(name = "file", required = false) MultipartFile[] fileList,
                                      @RequestParam(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList,
                                      HttpServletRequest request) {

        jwtService.getMemberIdFromToken(request);

        // 파일 5개 넘는지
        int fileCount = fileService.getFileCountByBoardId(boardId, Board.GALLERY_BOARD.getBoardType());
        multipartFileValidator.validateFileForModify(fileList, deleteFileIdList, fileCount);

        galleryBoardDto.setBoardId(boardId);

        boolean isThumbnailDeleted = false;

        // 파일 삭제
        if (deleteFileIdList != null) {
            // 썸네일 이미지 삭제인지
            isThumbnailDeleted = fileStorageService.deleteFileList(deleteFileIdList);
        }

        if (fileList != null) {
            // 파일 추가
            fileStorageService.storageFileList(fileList, boardId, Board.GALLERY_BOARD.getBoardType(), isThumbnailDeleted);
        } else if (isThumbnailDeleted) {
            // 추가 없이 썸네일 파일 삭제 포함 있을 경우
            FileDto fileDto = fileService.getFileListByBoardId(boardId, Board.GALLERY_BOARD.getBoardType()).get(0);
            fileStorageService.storageThumbnail(fileDto);
        }
        // 수정
        galleryBoardService.modifyBoard(galleryBoardDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 게시물 조회수 1 증가
     *
     * @param boardId PathVariable ( pk )
     * @return null
     */
    @PatchMapping("/board/gallery/{boardId}/increase-view")
    public ResponseEntity increaseView(@PathVariable(name = "boardId") Long boardId) {

        galleryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        galleryBoardService.increaseView(boardId);

        return ResponseEntity.ok().build();
    }

    /**
     * 작성자 확인
     *
     * @param boardId PathVariable ( pk )
     * @param request HttpServletRequest
     * @return true or false
     */
    @GetMapping("/board/gallery/{boardId}/check-author")
    public ResponseEntity checkAuthor(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {
        try {
            String memberId = jwtService.getMemberIdFromToken(request);
            galleryBoardService.getBoardByIdAndMemberId(boardId, memberId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        } catch (NotLoggedInException | BoardNotFoundException e) {
            throw new NotMyBoardException(ErrorCode.NOT_MY_BOARD);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 게시판 삭제
     *
     * @param boardId PathVariable ( pk )
     * @param request HttpServletRequest
     * @return null
     */
    @DeleteMapping("/board/gallery/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {

        jwtService.getMemberIdFromToken(request);

        galleryBoardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }
}
