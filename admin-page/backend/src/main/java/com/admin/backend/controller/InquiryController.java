package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.dto.AnswerDto;
import com.admin.backend.dto.InquiryDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.service.AnswerService;
import com.admin.backend.service.InquiryBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Inquiry Controller
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class InquiryController {

    private final InquiryBoardService inquiryBoardService;
    private final AnswerService answerService;

    /**
     * 문의게시판 리스트 GET
     *
     * @param model
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/inquiry")
    public String getListPage(Model model, @ModelAttribute SearchConditionDto searchConditionDto){

        int totalRowCount = inquiryBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        List<InquiryDto> inquiryDtoList = inquiryBoardService.getBoardListByCondition(searchConditionDto);

        model.addAttribute("boardList", inquiryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("totalPageNum", totalPageNum);

        return "/board/inquiry/inquiry-list";
    }

    /**
     * 문의게시판 보기 페이지 GET
     *
     * @param boardId
     * @param searchConditionDto
     * @param model
     * @return
     */
    @GetMapping("/board/inquiry/{boardId}")
    public String getViewPage(@PathVariable(name = "boardId")Long boardId, @ModelAttribute SearchConditionDto searchConditionDto, Model model){

        InquiryDto inquiryDto = inquiryBoardService.getBoardById(boardId).orElseThrow(()-> new BoardNotFoundException("잘못된 요청입니다."));
        AnswerDto answerDto = answerService.getAnswerByBoardId(boardId).orElseGet(AnswerDto::new);

        inquiryBoardService.increaseViewById(boardId);

        model.addAttribute("board", inquiryDto);
        model.addAttribute("answer", answerDto);
        model.addAttribute("searchCondition", searchConditionDto);

        return "/board/inquiry/inquiry-view";
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @GetMapping("/board/inquiry/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId")Long boardId){

        answerService.deleteAnswer(boardId);
        inquiryBoardService.deleteBoardById(boardId);

        return "redirect:/admin/board/inquiry";
    }

}
