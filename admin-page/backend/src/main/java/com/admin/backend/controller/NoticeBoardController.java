package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
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

        // 데이터 가져오기
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

    /**
     * 특정 게시물 페이지
     *
     * @param model
     * @param boardId
     * @param searchConditionDto
     * @return
     */
    @GetMapping("/board/notice/{boardId}")
    public String getBoardPage(Model model, @PathVariable(name = "boardId") Long boardId, @ModelAttribute SearchConditionDto searchConditionDto) {

        // 조회수 증가
        noticeBoardService.increaseView(boardId);

        // 데이터 가져오기
        NoticeBoardDto noticeBoardDto = noticeBoardService.getBoardByBoardId(boardId).orElseThrow(() -> new BoardNotFoundException("잘못된 요청입니다."));
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.NOTICE_BOARD.getBoardType());

        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("board", noticeBoardDto);

        return "/board/notice/notice-view";
    }

    /**
     * 게시물 수정
     *
     * @param boardId
     * @param noticeBoardDto
     * @return
     */
    @PostMapping("/board/notice/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId, @ModelAttribute NoticeBoardDto noticeBoardDto) {

        // 유효성 검증
        BoardValidator.validateNoticeBoard(noticeBoardDto);

        // 수정
        noticeBoardDto.setBoardId(boardId);
        noticeBoardService.modifyBoard(noticeBoardDto);

        // TODO : 수정 후 어디로? 수정 후 동일한 페이지로 redirect 하고도 검색 조건 유지?
        return "redirect:/admin/board/notice/" + boardId;
    }

    /**
     * 게시물 삭제
     *
     * @param boardId
     * @return
     */
    @GetMapping("/board/notice/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId) {
        // TODO : boardId와 일치하는 board가 있는지 확인 하는 로직이 필요할까

        // 삭제
        noticeBoardService.deleteBoardByBoardId(boardId);

        return "redirect:/admin/board/notice";
    }
}
