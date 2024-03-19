package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.type.Author;
import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.validator.BoardValidator;
import com.admin.backend.common.validator.SearchConditionValidator;
import com.admin.backend.dto.*;
import com.admin.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Free Board Controller
 */
@Slf4j
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FreeBoardController {

    private final CategoryService categoryService;
    private final FreeBoardService freeBoardService;
    private final FileService fileService;
    private final CommentService commentService;
    private final UploadService uploadService;

    /**
     * 자유 게시판 리스트 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건
     * @return /board/free/free-list.html
     */
    @GetMapping("/board/free")
    public String getListPage(Model model,
                              @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                              @ModelAttribute SearchConditionDto searchConditionDto) {

        // 검색조건 유효성 검증
        SearchConditionValidator.validateSearchCondition(searchConditionDto);

        // 페이지네이션 설정
        int totalRowCount = freeBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        // 데이터 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());
        List<FreeBoardDto> freeBoardDtoList = freeBoardService.getBoardListByCondition(searchConditionDto);

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("boardList", freeBoardDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("admin", adminDto);

        return "/board/free/free-list";
    }

    /**
     * 자유 게시판 쓰기 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/free/free-write.html
     */
    @GetMapping("/board/free/write")
    public String getWritePage(Model model,
                               @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                               @ModelAttribute SearchConditionDto searchConditionDto) {

        // 카테고리 리스트 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("admin", adminDto);

        return "/board/free/free-write";
    }

    /**
     * 자유 게시판 추가 POST
     *
     * @param freeBoardDto free-write form data ( categoryId , title, content, fileList )
     * @param adminDto     저장된 세션 정보 ( table column에 저장될 데이터 )
     * @return redirect:/admin/board/free
     */
    @PostMapping("/board/free/write")
    public String addBoard(@ModelAttribute FreeBoardDto freeBoardDto,
                           @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto) throws IOException {
        // 유효성 검증
        BoardValidator.validateFreeBoard(freeBoardDto);

        // author 세팅
        freeBoardDto.setAuthorType(Author.ADMIN.getAuthorType());
        freeBoardDto.setAuthorId(adminDto.getAdminId());

        // free board 추가 후 생성 된 pk return
        Long boardId = freeBoardService.addBoard(freeBoardDto);

        // file db 추가 후 upload
        if (freeBoardDto.getFile() != null) {
            List<FileDto> addedFileList = fileService.addFile(freeBoardDto.getFile(), Board.FREE_BOARD.getBoardType(), boardId);
            uploadService.uploadFile(addedFileList, freeBoardDto.getFile(), Board.FREE_BOARD.getBoardType());
        }

        return "redirect:/admin/board/free";
    }

    /**
     * 자유 게시판 보기 페이지 GET
     *
     * @param model              Model
     * @param boardId            PathVariable
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/free/free-view.html
     */
    @GetMapping("/board/free/{boardId}")
    public String getBoardPage(Model model,
                               @PathVariable(name = "boardId") Long boardId,
                               @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                               @ModelAttribute SearchConditionDto searchConditionDto) {

        // 데이터 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());
        FreeBoardDto freeBoardDto = freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));
        List<FileDto> fileDtoList = fileService.getFileListByBoardId(boardId, Board.FREE_BOARD.getBoardType());
        List<CommentDto> commentDtoList = commentService.getCommentListByBoardId(boardId, Board.FREE_BOARD.getBoardType());
        int fileCountInBoard = fileService.getRowCountByBoardId(boardId, Board.FREE_BOARD.getBoardType());

        // 조회수 증가
        freeBoardService.increaseView(boardId);

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("board", freeBoardDto);
        model.addAttribute("fileList", fileDtoList);
        model.addAttribute("commentList", commentDtoList);
        model.addAttribute("fileCountInBoard", fileCountInBoard);
        model.addAttribute("admin", adminDto);

        return "/board/free/free-view";
    }

    /**
     * 자유 게시판 수정 POST
     *
     * @param boardId      PathVariable
     * @param freeBoardDto free-modify form data ( categoryId , title, content, fileList, deletedFileIdList )
     * @return redirect:/admin/board/free
     */
    @PostMapping("/board/free/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute FreeBoardDto freeBoardDto) throws IOException {

        // 유효성 검증에 필요한 현재 파일 개수
        int fileCountInBoard = fileService.getRowCountByBoardId(boardId, Board.FREE_BOARD.getBoardType());

        // 유효성 검증
        BoardValidator.validateFreeBoard(freeBoardDto, fileCountInBoard);

        // boardId 세팅
        freeBoardDto.setBoardId(boardId);

        // 수정 및 삭제
        freeBoardService.modifyBoard(freeBoardDto);
        if (freeBoardDto.getDeleteFileIdList() != null) {
            fileService.deleteFileList(freeBoardDto.getDeleteFileIdList());
        }

        // file db 추가 후 upload
        if (freeBoardDto.getFile() != null) {
            List<FileDto> fileDtoList = fileService.addFile(freeBoardDto.getFile(), Board.FREE_BOARD.getBoardType(), boardId);
            uploadService.uploadFile(fileDtoList, freeBoardDto.getFile(), Board.FREE_BOARD.getBoardType());
        }

        return "redirect:/admin/board/free";
    }

    /**
     * 자유 게시판 삭제
     *
     * @param boardId PathVariable
     * @return redirect:/admin/board/free
     */
    @GetMapping("/board/free/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId) {

        // 삭제
        freeBoardService.deleteBoard(boardId);

        return "redirect:/admin/board/free";
    }
}
