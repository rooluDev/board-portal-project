package com.admin.backend.controller;

import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.validator.BoardValidator;
import com.admin.backend.dto.AdminDto;
import com.admin.backend.dto.CategoryDto;
import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.service.CategoryService;
import com.admin.backend.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Notice Board Controller
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;
    private final CategoryService categoryService;

    /**
     * 공지사항 리스트 페이지
     *
     * @param model
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/notice")
    public String getListPage(Model model, @ModelAttribute SearchConditionDto searchConditionDto) {

        // 페이지네이션 설정
        int totalRowCount = noticeBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        // 필요한 데이터 가져오기
        List<CategoryDto> categoryList = categoryService.getCategoryListByBoardType(Board.NOTICE_BOARD.getBoardType());
        List<NoticeBoardDto> noticeBoardList = noticeBoardService.getBoardListByCondition(searchConditionDto);
        List<NoticeBoardDto> fixedBoardList = noticeBoardService.getFixedBoardList();

        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("boardList", noticeBoardList);
        model.addAttribute("fixedBoardList", fixedBoardList);

        return "/board/notice/notice-list";
    }

    /**
     * 공지사항 쓰기 페이지
     *
     * @param model
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/notice/write")
    public String getWritePage(Model model, @ModelAttribute SearchConditionDto searchConditionDto) {

        // 카테고리 리스트 가져오기
        List<CategoryDto> categoryList = categoryService.getCategoryListByBoardType(Board.NOTICE_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("searchCondition", searchConditionDto);

        return "/board/notice/notice-write";
    }

    /**
     * 공지사항 Add proc
     *
     * @param noticeBoardDto
     * @param adminDto
     * @return
     */
    @PostMapping("/board/notice/write")
    public String addBoard(@ModelAttribute NoticeBoardDto noticeBoardDto, @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto) {

        // 유효성 검증
        BoardValidator.validateNoticeBoard(noticeBoardDto);

        // 사용자 ID set
        noticeBoardDto.setAuthorId(adminDto.getAdminId());

        // 저장
        noticeBoardService.addBoard(noticeBoardDto);

        return "redirect:/admin/board/notice";
    }
}
