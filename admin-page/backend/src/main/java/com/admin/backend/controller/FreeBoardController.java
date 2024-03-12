package com.admin.backend.controller;

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
    public String getWritePage(Model model) {

        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.FREE_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryDtoList);

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
}
