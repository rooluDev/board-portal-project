package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.type.Author;
import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.validator.BoardValidator;
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
     * 자유게시판 리스트 페이지
     *
     * @param model
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/free")
    public String getListPage(Model model, @ModelAttribute SearchConditionDto searchConditionDto) {

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

        return "/board/free/free-list";
    }

    /**
     * 자유게시판 쓰기 페이지
     *
     * @param model
     * @return
     */
    @GetMapping("/board/free/write")
    public String getWritePage(Model model, @ModelAttribute SearchConditionDto searchConditionDto) {
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);

        return "/board/free/free-write";
    }

    /**
     * 자유게시판 Add proc
     *
     * @param freeBoardAddDto
     * @param adminDto
     * @return
     */
    @PostMapping("/board/free/write")
    public String addBoard(@ModelAttribute FreeBoardAddDto freeBoardAddDto, @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto) throws IOException {
        // 유효성 검증
        BoardValidator.validateFreeBoard(freeBoardAddDto);

        FreeBoardDto freeBoardDto = FreeBoardDto.builder()
                .categoryId(freeBoardAddDto.getCategoryId())
                .title(freeBoardAddDto.getTitle())
                .content(freeBoardAddDto.getContent())
                .authorType(Author.ADMIN.getAuthorType())
                .authorId(adminDto.getAdminId())
                .build();

        // free board 추가
        Long boardId = freeBoardService.addBoard(freeBoardDto);

        // file 추가
        if (freeBoardAddDto.getFile() != null) {
            List<FileDto> addedFileList = fileService.addFile(freeBoardAddDto.getFile(), Board.FREE_BOARD.getBoardType(), boardId);
            uploadService.uploadFile(addedFileList, freeBoardAddDto.getFile());
        }

        return "redirect:/admin/board/free";
    }

    /**
     * 자유 게시판 보기 페이지
     *
     * @param boardId
     * @param model
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/free/{boardId}")
    public String getBoardPage(@PathVariable(name = "boardId") Long boardId, Model model, @ModelAttribute SearchConditionDto searchConditionDto) {
        // 조회수 증가
        freeBoardService.increaseView(boardId);

        // 데이터 가져오기
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());
        FreeBoardDto freeBoardDto = freeBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));
        List<FileDto> fileDtoList = fileService.getFileListByBoardId(boardId, Board.FREE_BOARD.getBoardType());
        List<CommentDto> commentDtoList = commentService.getCommentListByBoardId(boardId, Board.FREE_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("board", freeBoardDto);
        model.addAttribute("fileList", fileDtoList);
        model.addAttribute("commentList", commentDtoList);

        return "/board/free/free-view";
    }

    /**
     * 게시물 수정
     *
     * @param boardId
     * @param freeBoardAddDto
     * @return
     */
    @PostMapping("/board/free/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId, @ModelAttribute FreeBoardAddDto freeBoardAddDto) {
        // TODO : MODIFY

        return "redirect:/admin/board/free";
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @GetMapping("/board/free/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId) {
        // TODO : DELETE, isDeleted 필요?
        freeBoardService.deleteBoard(boardId);


        return "redirect:/admin/board/free";
    }
}
