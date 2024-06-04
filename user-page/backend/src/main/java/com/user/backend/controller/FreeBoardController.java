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
import com.user.backend.common.validator.constraint.FreeBoardFileConstraint;
import com.user.backend.dto.*;
import com.user.backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Free Board Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FreeBoardController {

    private final FreeBoardService freeBoardService;
    private final CategoryService categoryService;
    private final FileService fileService;
    private final CommentService commentService;
    private final JwtService jwtService;
    private final FileStorageService fileStorageService;
    private final MultipartFileValidator<FreeBoardFileConstraint> multipartFileValidator;

    /**
     * 자유게시판 리스트 페이지에 필요한 데이터 반환
     *
     * @param searchConditionDto 검색조건
     * @return {
     * freeBoardList:[],
     * categoryList:[],
     * searchCondition:{},
     * totalPageNum:0
     * }
     */
    @GetMapping("/boards/free")
    public ResponseEntity<Map> getBoardList(@ModelAttribute SearchConditionDto searchConditionDto) {

        int totalRowCount = freeBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        List<FreeBoardDto> freeBoardList = freeBoardService.getBoardListByCondition(searchConditionDto);
        List<CategoryDto> categoryList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());

        Map response = new HashMap();
        response.put("freeBoardList", freeBoardList);
        response.put("categoryList", categoryList);
        response.put("searchCondition", searchConditionDto);
        response.put("totalPageNum", totalPageNum);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 자유 게시판 보기 페이지 및 수정 페이지에 필요한 데이터 반환
     *
     * @param boardId PathVariable ( pk )
     * @return {
     * freeBoard: {},
     * commentList:[],
     * fileList:[],
     * categoryList:[]
     * }
     */
    @GetMapping("/board/free/{boardId}")
    public ResponseEntity<Map> getBoard(@PathVariable(name = "boardId") Long boardId) {

        FreeBoardDto freeBoard = freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        List<CommentDto> commentList = commentService.getCommentListByBoardId(boardId, Board.FREE_BOARD.getBoardType());
        List<FileDto> fileList = fileService.getFileListByBoardId(boardId, Board.FREE_BOARD.getBoardType());
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());

        Map response = new HashMap();
        response.put("freeBoard", freeBoard);
        response.put("commentList", commentList);
        response.put("fileList", fileList);
        response.put("categoryList", categoryDtoList);

        return ResponseEntity.ok(response);
    }

    /**
     * 자유게시판 추가
     *
     * @param freeBoardDto 추가할 데이터
     * @param fileList     추가할 첨부파일
     * @param request      HttpServletRequest
     * @return null
     */
    @PostMapping("/board/free")
    public ResponseEntity addBoard(@Valid @ModelAttribute FreeBoardDto freeBoardDto,
                                   @RequestPart(name = "file", required = false) MultipartFile[] fileList,
                                   HttpServletRequest request) {

        if (fileList != null) {
            multipartFileValidator.validateFile(fileList);
        }

        // 작성자 데이터 세팅
        String memberId = jwtService.getMemberIdFromToken(request);
        freeBoardDto.setAuthorType(Author.MEMBER.getAuthorType());
        freeBoardDto.setAuthorId(memberId);

        // text부분 추가 후 저장된 pk 반환
        Long boardId = freeBoardService.addBoard(freeBoardDto);

        // 파일 추가
        if (fileList != null) {
            fileStorageService.storageFileList(fileList, boardId, Board.FREE_BOARD.getBoardType(), false);
        }

        return ResponseEntity.ok().build();
    }

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

        // 토근 확인
        jwtService.getMemberIdFromToken(request);

        int fileCount = fileService.getFileCountByBoardId(boardId, Board.FREE_BOARD.getBoardType());

        multipartFileValidator.validateFileForModify(fileList, deleteFileIdList, fileCount);

        freeBoardDto.setBoardId(boardId);

        // text부분 수정
        freeBoardService.modifyBoard(freeBoardDto);

        // 파일 삭제
        if (deleteFileIdList != null) {
            fileStorageService.deleteFileList(deleteFileIdList);
        }
        // 파일 추가
        if (fileList != null) {
            fileStorageService.storageFileList(fileList, boardId, Board.FREE_BOARD.getBoardType(), false);
        }

        return ResponseEntity.ok().build();
    }

    /**
     * 게시물 조회수 1 증가
     *
     * @param boardId PathVariable ( pk )
     * @return null
     */
    @PatchMapping("/board/free/{boardId}/increase-view")
    public ResponseEntity increaseView(@PathVariable(name = "boardId") Long boardId) {

        freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        freeBoardService.increaseView(boardId);

        return ResponseEntity.ok().build();
    }

    /**
     * 작성자 확인
     *
     * @param boardId PathVariable ( pk )
     * @param request HttpServletRequest
     * @return true or false
     */
    @GetMapping("/board/free/{boardId}/check-author")
    public ResponseEntity checkAuthor(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {
        try {
            String memberId = jwtService.getMemberIdFromToken(request);
            freeBoardService.getBoardByIdAndMemberId(boardId, memberId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
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
    @DeleteMapping("/board/free/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {

        jwtService.getMemberIdFromToken(request);

        freeBoardService.deleteBoard(boardId);

        return ResponseEntity.ok().build();
    }
}
