package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.exception.StorageFailException;
import com.admin.backend.common.type.Author;
import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.utils.StringUtils;
import com.admin.backend.common.validator.BoardValidator;
import com.admin.backend.common.validator.SearchConditionValidator;
import com.admin.backend.dto.*;
import com.admin.backend.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Free Board Controller
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class FreeBoardController {

    private final CategoryService categoryService;
    private final FreeBoardService freeBoardService;
    private final FileService fileService;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;

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
        // TODO : 검색조건 유효성 검증 및 검색조건 유지
//        SearchConditionValidator.validateSearchCondition(searchConditionDto);

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
     * @param freeBoardDto       free-write form data ( categoryId , title, content, fileList )
     * @param adminDto           저장된 세션 정보 ( table column에 저장될 데이터 )
     * @param redirectAttributes 등록 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/free
     */
    @PostMapping("/board/free/write")
    public String addBoard(@ModelAttribute FreeBoardDto freeBoardDto,
                           @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                           @RequestParam(name = "file", required = false) MultipartFile[] files,
                           RedirectAttributes redirectAttributes) {

        // 유효성 검증
        // TODO : 유효성 검증 및 검색조건 유지
//        BoardValidator.validateFreeBoard(freeBoardDto);

        // author 세팅
        freeBoardDto.setAuthorType(Author.ADMIN.getAuthorType());
        freeBoardDto.setAuthorId(adminDto.getAdminId());

        // free board 추가 후 생성 된 pk return
        Long boardId = freeBoardService.addBoard(freeBoardDto);

        // storage 저장 후 DB 저장
        if (files != null) {
            try {
                fileStorageService.fileStorage(files, boardId, Board.FREE_BOARD.getBoardType());
            } catch (IOException e) {
                new StorageFailException();
            }
        }

        // 등록 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("write", 1);

        return "redirect:/board/free";
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
        FreeBoardDto freeBoardDto = freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException());
        List<FileDto> fileDtoList = fileService.getFileListByBoardId(boardId, Board.FREE_BOARD.getBoardType());
        List<CommentDto> commentDtoList = commentService.getCommentListByBoardId(boardId, Board.FREE_BOARD.getBoardType());

        // 조회수 증가
        freeBoardService.increaseView(boardId);

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("board", freeBoardDto);
        model.addAttribute("fileList", fileDtoList);
        model.addAttribute("commentList", commentDtoList);
        model.addAttribute("admin", adminDto);

        return "/board/free/free-view";
    }

    /**
     * 자유 게시판 수정 POST
     *
     * @param boardId            PathVariable
     * @param freeBoardDto       free-modify form data ( categoryId , title, content, fileList, deletedFileIdList )
     * @param searchConditionDto 수정 후 검색조건을 유지하기 위한 쿼리스트링
     * @param redirectAttributes 수정 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/free
     */
    @PostMapping("/board/free/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute FreeBoardDto freeBoardDto,
                              @RequestParam(name = "file", required = false) MultipartFile[] files,
                              @RequestParam(name = "deleteFileIdList", required = false) List<Long> deleteFileIdList,
                              @ModelAttribute SearchConditionDto searchConditionDto,
                              RedirectAttributes redirectAttributes) {

        // boardId 유효성 검증
        freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException());

        // 유효성 검증에 필요한 현재 파일 개수
        int fileCountInBoard = fileService.getRowCountByBoardId(boardId, Board.FREE_BOARD.getBoardType());

        // 유효성 검증
//        BoardValidator.validateFreeBoard(freeBoardDto, fileCountInBoard);

        // boardId 세팅
        freeBoardDto.setBoardId(boardId);

        // 수정 및 삭제
        freeBoardService.modifyBoard(freeBoardDto);
        if (deleteFileIdList != null) {
            fileService.deleteFileList(deleteFileIdList);
        }

        if (files != null) {
            try {
                fileStorageService.fileStorage(files, boardId, Board.FREE_BOARD.getBoardType());
            } catch (IOException e) {
                new StorageFailException();
            }
        }

        // 수정 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("modify", 1);

        return "redirect:/board/free/" + boardId + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
    }

    /**
     * 자유 게시판 삭제 GET
     *
     * @param boardId            PathVariable
     * @param searchConditionDto 삭제 완료 후 검색 조건 유지를 위한 쿼리스트링
     * @param redirectAttributes 삭제 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/free
     */
    @GetMapping("/board/free/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute SearchConditionDto searchConditionDto,
                              RedirectAttributes redirectAttributes) {

        // boardId 유효성 검증
        freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException());

        // 삭제
        freeBoardService.deleteBoard(boardId);

        // 삭제 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("delete", 1);

        return "redirect:/board/free" + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
    }
}
