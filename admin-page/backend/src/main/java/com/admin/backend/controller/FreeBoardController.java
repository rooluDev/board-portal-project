package com.admin.backend.controller;

import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.dto.CategoryDto;
import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.service.CategoryService;
import com.admin.backend.service.CommentService;
import com.admin.backend.service.FileService;
import com.admin.backend.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

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

    /**
     * 자유게시판 리스트 페이지
     *
     * @param model
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/free")
    public String getListPage(Model model, @ModelAttribute SearchConditionDto searchConditionDto){

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

}
