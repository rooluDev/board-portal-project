package com.admin.backend.service;

import com.admin.backend.dto.InquiryBoardDto;
import com.admin.backend.dto.SearchConditionDto;

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
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 리스트
     */
    List<InquiryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto);

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
    void increaseViewById(Long boardId);
}
