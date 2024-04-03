package com.admin.backend.controller;

import com.admin.backend.common.exception.BoardNotFoundException;
import com.admin.backend.common.type.Board;
import com.admin.backend.common.utils.PaginationUtils;
import com.admin.backend.common.utils.StringUtils;
import com.admin.backend.common.validator.BoardValidator;
import com.admin.backend.common.validator.SearchConditionValidator;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * Notice Board Controller
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;
    private final CategoryService categoryService;

    /**
     * 공지사항 게시판 리스트 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건
     * @return /board/notice/notice-list.html
     */
    @GetMapping("/board/notice")
    public String getListPage(Model model,
                              @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                              @ModelAttribute SearchConditionDto searchConditionDto) {

        // 검색조건 유효성 검증
        // TODO : 검색조건 유효성 검증 및 검색조건 유지
//        SearchConditionValidator.validateSearchCondition(searchConditionDto);

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
        model.addAttribute("admin", adminDto);

        return "/board/notice/notice-list";
    }

    /**
     * 공지사항 게시판 쓰기 페이지 GET
     *
     * @param model              Model
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/notice/notice-write.html
     */
    @GetMapping("/board/notice/write")
    public String getWritePage(Model model,
                               @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                               @ModelAttribute SearchConditionDto searchConditionDto) {

        // 카테고리 리스트 가져오기
        List<CategoryDto> categoryList = categoryService.getCategoryListByBoardType(Board.NOTICE_BOARD.getBoardType());

        model.addAttribute("categoryList", categoryList);
        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("admin", adminDto);

        return "/board/notice/notice-write";
    }

    /**
     * 공지사항 게시판 추가 POST
     *
     * @param noticeBoardDto     notice-write form data ( categoryId , title, content, fixed )
     * @param adminDto           저장된 세션 정보 ( table column에 저장될 데이터 )
     * @param searchConditionDto Exception 발생시 검색조건을 유지하기 위한 쿼리스트링
     * @param redirectAttributes 등록 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/notice
     */
    @PostMapping("/board/notice/write")
    public String addBoard(@ModelAttribute NoticeBoardDto noticeBoardDto,
                           @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                           @ModelAttribute SearchConditionDto searchConditionDto,
                           RedirectAttributes redirectAttributes) {

        // 유효성 검증
        // TODO : 검색조건 유효성 검증 및 검색조건 유지
//        BoardValidator.validateNoticeBoard(noticeBoardDto);

        // author 세팅
        noticeBoardDto.setAuthorId(adminDto.getAdminId());

        // 저장
        noticeBoardService.addBoard(noticeBoardDto);

        // 등록 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("write", 1);

        return "redirect:/board/notice";
    }

    /**
     * 공지사항 게시판 보기 페이지 GET
     *
     * @param model              Model
     * @param boardId            PathVariable
     * @param adminDto           저장된 세션 정보 ( nav에 필요한 정보 )
     * @param searchConditionDto 게시물 검색 조건 (검색 조건을 유지하기 위한 데이터)
     * @return /board/notice/notice-view.html
     */
    @GetMapping("/board/notice/{boardId}")
    public String getBoardPage(Model model,
                               @PathVariable(name = "boardId") Long boardId,
                               @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto,
                               @ModelAttribute SearchConditionDto searchConditionDto) {

        // 데이터 가져오기
        NoticeBoardDto noticeBoardDto = noticeBoardService.getBoardByBoardId(boardId).orElseThrow(() -> new BoardNotFoundException());
        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(Board.NOTICE_BOARD.getBoardType());

        // 조회수 증가
        noticeBoardService.increaseView(boardId);

        model.addAttribute("searchCondition", searchConditionDto);
        model.addAttribute("categoryList", categoryDtoList);
        model.addAttribute("board", noticeBoardDto);
        model.addAttribute("admin", adminDto);

        return "/board/notice/notice-view";
    }

    /**
     * 공지사항 게시판 수정 POST
     *
     * @param boardId            PathVariable
     * @param noticeBoardDto     notice-modify form data ( categoryId , title, content, fixed )
     * @param searchConditionDto 수정 후 검색조건을 유지하기 위한 쿼리스트링
     * @param redirectAttributes 수정 완료 alert를 위한 redirectAttributes
     * @return redirect:/board/notice/" + boardId
     */
    @PostMapping("/board/notice/modify/{boardId}")
    public String modifyBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute NoticeBoardDto noticeBoardDto,
                              @ModelAttribute SearchConditionDto searchConditionDto,
                              RedirectAttributes redirectAttributes) {

        // 유효성 검증
        // TODO : 검색조건 유효성 검증 및 검색조건 유지
//        BoardValidator.validateNoticeBoard(noticeBoardDto);

        // 수정
        noticeBoardDto.setBoardId(boardId);
        noticeBoardService.modifyBoard(noticeBoardDto);

        // 수정 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("modify", 1);

        return "redirect:/board/notice/" + boardId + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
    }

    /**
     * 공지사항 게시판 삭제 GET
     *
     * @param boardId            PathVariable
     * @param redirectAttributes 삭제 완료 alert를 위한 redirectAttributes
     * @param searchConditionDto 삭제 완료 후 검색 조건 유지를 위한 쿼리스트링
     * @return redirect:/board/notice
     */
    @GetMapping("/board/notice/delete/{boardId}")
    public String deleteBoard(@PathVariable(name = "boardId") Long boardId,
                              @ModelAttribute SearchConditionDto searchConditionDto,
                              RedirectAttributes redirectAttributes) {

        // boardId 유효성 검증
        noticeBoardService.getBoardByBoardId(boardId).orElseThrow(() -> new BoardNotFoundException());

        // 삭제
        noticeBoardService.deleteBoardByBoardId(boardId);

        // 삭제 완료 응답 값 설정
        redirectAttributes.addFlashAttribute("delete", 1);

        return "redirect:/board/notice" + StringUtils.searchConditionToQueryStringWithCategory(searchConditionDto);
    }
}
