package com.user.backend.controller;


import com.user.backend.common.exception.custom.BoardNotFoundException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.type.Board;
import com.user.backend.common.utils.PaginationUtils;
import com.user.backend.dto.CategoryDto;
import com.user.backend.dto.NoticeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.service.CategoryService;
import com.user.backend.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notice Board Controller
 */
@RestController
@RequestMapping("/api")
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;
    private final CategoryService categoryService;

    public NoticeBoardController(@Qualifier("noticeBoardJpa") NoticeBoardService noticeBoardService,
                                 @Qualifier("categoryJpa") CategoryService categoryService) {
        this.noticeBoardService = noticeBoardService;
        this.categoryService = categoryService;
    }

    /**
     * 검색조건에 따른 공지사항 게시판 데이터 GET
     *
     * @param searchConditionDto 검색조건
     * @return {
     * totalPageNUm: 0,
     * noticeBoardList: [],
     * fixedNoticeBoardList: [],
     * cateogryList: [],
     * searchCondition: {}
     * }
     */
    @GetMapping("/boards/notice")
    public ResponseEntity<Map> getBoardList(@ModelAttribute SearchConditionDto searchConditionDto) {

        // 데이터 가져오기
        List<NoticeBoardDto> noticeBoardList = noticeBoardService.getBoardListByCondition(searchConditionDto);
        List<NoticeBoardDto> fixedNoticeBoardList = noticeBoardService.getFixedBoardList();
        List<CategoryDto> categoryList = categoryService.getCategoryListByBoardType(Board.NOTICE_BOARD.getBoardType());

        // 페이지네이션 설정
        int totalRowCount = noticeBoardService.getTotalRowCountByCondition(searchConditionDto);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        Map response = new HashMap();
        response.put("totalPageNum", totalPageNum);
        response.put("noticeBoardList", noticeBoardList);
        response.put("fixedNoticeBoardList", fixedNoticeBoardList);
        response.put("categoryList", categoryList);
        response.put("searchCondition", searchConditionDto);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 공지사항 게시판 보기 페이지에 필요한 데이터
     *
     * @param boardId PathVariable ( pk )
     * @return noticeBoard
     */
    @GetMapping("/board/notice/{boardId}")
    public ResponseEntity<NoticeBoardDto> getBoard(@PathVariable(name = "boardId") Long boardId) {

        // 데이터 가져오기
        NoticeBoardDto noticeBoardDto = noticeBoardService.getBoardByBoardId(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        return ResponseEntity.ok().body(noticeBoardDto);
    }

    /**
     * 게시판 조회수 1 증가
     *
     * @param boardId PathVariable ( pk )
     * @return null
     */
    @PatchMapping("/board/notice/{boardId}/increase-view")
    public ResponseEntity increaseView(@PathVariable(name = "boardId") Long boardId) {

        // boardId 검증
        noticeBoardService.getBoardByBoardId(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        // 조회수 증가
        noticeBoardService.increaseView(boardId);

        return ResponseEntity.ok().build();
    }

}
