package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.validator.SearchConditionValidator;
import com.admin.backend.dto.AdminDto;
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
     * 문의 게시판 리스트 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건
     * @return /board/inquiry/inquiry-list.html
     */
    @GetMapping("/board/inquiry")
    public String getListPage(Model model,
                              @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                              @ModelAttribute SearchConditionDto searchConditionDto) {

        // 검색조건 유효성 검증
        SearchConditionValidator.validateSearchCondition(searchConditionDto);

        // 페이지네이션 설정
        int totalRowCount = inquiryBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        // 데이터 가져오기
        List<InquiryDto> inquiryDtoList = inquiryBoardService.getBoardListByCondition(searchConditionDto);

        model.addAttribute("boardList", inquiryDtoList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("totalPageNum", totalPageNum);
        model.addAttribute("admin", adminDto);

        return "/board/inquiry/inquiry-list";
    }

    /**
     * 문의 게시판 보기 페이지 GET
     *
     * @param model              Model
     * @param boardId            PathVariable
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/inquiry/inquiry-view.html
     */
    @GetMapping("/board/inquiry/{boardId}")
    public String getViewPage(Model model,
                              @PathVariable(name = "boardId") Long boardId,
                              @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                              @ModelAttribute SearchConditionDto searchConditionDto) {

        // 데이터 가져오기
        InquiryDto inquiryDto = inquiryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));
        AnswerDto answerDto = answerService.getAnswerByBoardId(boardId).orElseGet(AnswerDto::new);

        // 조회수 증가
        inquiryBoardService.increaseViewById(boardId);

        model.addAttribute("board", inquiryDto);
        model.addAttribute("answer", answerDto);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("admin", adminDto);

        return "/board/inquiry/inquiry-view";
    }

    /**
     * 문의 게시판 삭제
     *
     * @param boardId PathVariable
     * @return redirect:/admin/board/inquiry
     */
    @GetMapping("/board/inquiry/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId) {

        // 삭제
        answerService.deleteAnswer(boardId);
        inquiryBoardService.deleteBoardById(boardId);

        return "redirect:/admin/board/inquiry";
    }

}
