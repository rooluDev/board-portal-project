package com.user.backend.service;

import com.user.backend.dto.InquiryBoardDto;
import com.user.backend.dto.SearchConditionDto;

import java.util.List;
import java.util.Optional;

/**
 * Inquiry Board Service Interface
 */
public interface InquiryBoardService {

    /**
     * 검색조건에 맞는 문의 게시물 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 문의 게시물 총 개수
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId);

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 리스트
     */
    List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto, String memberId);

    /**
     * 문의 게시물 찾기
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 문의 게시물
     */
    Optional<InquiryBoardDto> getBoardById(Long boardId);

    /**
     * 문의 게시물 삭제
     *
     * @param boardId ( pk )
     */
    void deleteBoardById(Long boardId);

    /**
     * 조회수 증가
     *
     * @param boardId ( pk )
     */
    void increaseView(Long boardId);

    /**
     * 메인 페이지에 필요한 문의 게시판 리스트 가져오기
     *
     * @return 메인 페이지에 필요한 문의 게시판 리스트
     */
    List<InquiryBoardDto> getBoardListForMain();

    /**
     * 문의 게시판 추가
     *
     * @param inquiryBoardDto ( author_id, title, content, is_secret )
     */
    void addBoard(InquiryBoardDto inquiryBoardDto);

    /**
     * 문의 게시판 수정
     *
     * @param inquiryBoardDto ( title, content, is_secret )
     */
    void modifyBoard(InquiryBoardDto inquiryBoardDto);

    /**
     * boardId와 memberId가 일치하는 게시물 가져오기
     *
     * @param boardId ( pk )
     * @param memberId memberId
     * @return boardId와 memberId가 일치하는 게시물
     */
    Optional<InquiryBoardDto> getBoardByIdAndMemberId(Long boardId, String memberId);
}
