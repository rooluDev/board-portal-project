package com.user.backend.controller;

import com.user.backend.common.exception.custom.BoardNotFoundException;
import com.user.backend.common.exception.custom.NotLoggedInException;
import com.user.backend.common.exception.custom.NotMyBoardException;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.utils.PaginationUtils;
import com.user.backend.dto.AnswerDto;
import com.user.backend.dto.InquiryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import com.user.backend.service.AnswerService;
import com.user.backend.service.InquiryBoardService;
import com.user.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Inquiry Board Controller
 */
@RestController
@RequestMapping("/api")
public class InquiryBoardController {

    private final AnswerService answerService;
    private final InquiryBoardService inquiryBoardService;
    private final JwtService jwtService;

    public InquiryBoardController(@Qualifier("answerJpa") AnswerService answerService,
                                  @Qualifier("inquiryBoardJpa") InquiryBoardService inquiryBoardService,
                                  JwtService jwtService) {
        this.answerService = answerService;
        this.inquiryBoardService = inquiryBoardService;
        this.jwtService = jwtService;
    }

    /**
     * 문의리게시판 리스트 페이지에 필요한 데이터 반환
     *
     * @param searchConditionDto 검색조건
     * @param request            HttpServletRequest
     * @return {
     * totalPageNum: 0,
     * inquiryBoardList: [],
     * searchCondition:{}
     * }
     */
    @GetMapping("/boards/inquiry")
    public ResponseEntity<Map> getBoardList(@ModelAttribute SearchConditionDto searchConditionDto, HttpServletRequest request) {

        String memberId = null;
        // 나의 문의 내역 체크
        if (searchConditionDto.getMy().equals("true")) {
            try {
                memberId = jwtService.getMemberIdFromToken(request);
            } catch (NotLoggedInException e) {
                memberId = null;
            }
        }

        // 페이지네이션 설정
        int totalRowCount = inquiryBoardService.getTotalRowCountByCondition(searchConditionDto, memberId);
        int totalPageNum = PaginationUtils.getTotalPageNum(totalRowCount, searchConditionDto.getPageSize());

        // 데이터 가져오기
        List<InquiryBoardDto> inquiryBoardDtoList = inquiryBoardService.getBoardListByCondition(searchConditionDto, memberId);


        Map response = new HashMap();
        response.put("totalPageNum", totalPageNum);
        response.put("inquiryBoardList", inquiryBoardDtoList);
        response.put("searchCondition", searchConditionDto);

        return ResponseEntity.ok(response);
    }

    /**
     * 문의게시판 보기 페이지 및 수정 페이지에 필요한 데이터 반환
     *
     * @param boardId PathVariable ( pk )
     * @param request HttpServletRequest
     * @return {
     * inquiryBoard:{},
     * answer:{}
     * }
     */
    @GetMapping("/board/inquiry/{boardId}")
    public ResponseEntity<Map> getBoard(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {

        // boardId 검증
        InquiryBoardDto inquiryBoardDto = inquiryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        // 비밀글 체크 후 작성자 확인
        if (inquiryBoardDto.getIsSecret().equals("1")) {
            String memberId = jwtService.getMemberIdFromToken(request);
            inquiryBoardService.getBoardByIdAndMemberId(boardId, memberId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        }

        // 답변 데이터 가져오기
        AnswerDto answerDto = answerService.getAnswerByBoardId(boardId).orElseGet(AnswerDto::new);

        Map response = new HashMap();
        response.put("inquiryBoard", inquiryBoardDto);
        response.put("answer", answerDto);

        return ResponseEntity.ok(response);
    }


    /**
     * 게시판 추가
     *
     * @param inquiryBoardDto 추가할 데이터
     * @param request         HttpServletRequest
     * @return null
     */
    @PostMapping("/board/inquiry")
    public ResponseEntity addBoard(@Valid @RequestBody InquiryBoardDto inquiryBoardDto,
                                   HttpServletRequest request) {

        // 로그인 확인 및 author 세팅
        String memberId = jwtService.getMemberIdFromToken(request);
        inquiryBoardDto.setAuthorId(memberId);

        // 추가
        inquiryBoardService.addBoard(inquiryBoardDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 게시판 수정
     *
     * @param boardId         PathVariable ( pk )
     * @param inquiryBoardDto 수정할 데이터
     * @param request         HttpServletRequest
     * @return null
     */
    @PutMapping("/board/inquiry/{boardId}")
    public ResponseEntity modifyBoard(@PathVariable(name = "boardId") Long boardId,
                                      @Valid @RequestBody InquiryBoardDto inquiryBoardDto,
                                      HttpServletRequest request) {
        // 로그인 확인
        jwtService.getMemberIdFromToken(request);

        // boardId 검증
        inquiryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        // 수정
        inquiryBoardDto.setBoardId(boardId);
        inquiryBoardService.modifyBoard(inquiryBoardDto);

        return ResponseEntity.ok().build();
    }

    /**
     * 조회수 1 증가
     *
     * @param boardId PathVariable ( pk )
     * @return null
     */
    @PatchMapping("/board/inquiry/{boardId}/increase-view")
    public ResponseEntity increaseView(@PathVariable(name = "boardId") Long boardId) {

        // boardId 검증
        inquiryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));

        // 조회수 증가
        inquiryBoardService.increaseView(boardId);

        return ResponseEntity.ok().build();
    }

    /**
     * 작성자 확인
     *
     * @param boardId PathVariable ( pk )
     * @param request HttpServletRequest
     * @return true or false
     */
    @GetMapping("/board/inquiry/{boardId}/check-author")
    public ResponseEntity checkAuthor(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {
        try {
            // 로그인 확인
            String memberId = jwtService.getMemberIdFromToken(request);
            // 게시판 본인 확인
            inquiryBoardService.getBoardByIdAndMemberId(boardId, memberId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
        } catch (NotLoggedInException | BoardNotFoundException e) {
            throw new NotMyBoardException(ErrorCode.NOT_MY_BOARD);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * 게시판 삭제
     *
     * @param boardId PathVariable ( pk )
     * @param request HttpServletRequest
     * @return null
     */
    @DeleteMapping("/board/inquiry/{boardId}")
    public ResponseEntity deleteBoard(@PathVariable(name = "boardId") Long boardId, HttpServletRequest request) {
        try {
            // 로그인 확인
            String memberId = jwtService.getMemberIdFromToken(request);
            // 게시판 본인 확인
            inquiryBoardService.getBoardById(boardId).orElseThrow(() -> new BoardNotFoundException(ErrorCode.BOARD_NOT_FOUND));
            // 삭제
            inquiryBoardService.deleteBoardById(boardId);
        } catch (NotLoggedInException | BoardNotFoundException e) {
            throw new NotMyBoardException(ErrorCode.NOT_MY_BOARD);
        }

        return ResponseEntity.ok().build();
    }
}
