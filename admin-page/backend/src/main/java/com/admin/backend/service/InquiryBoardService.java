package com.admin.backend.service;

import com.admin.backend.dto.InquiryDto;
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
     * @param searchConditionDto
     * @return
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 가져오기
     *
     * @param searchConditionDto
     * @return
     */
    List<InquiryDto> getBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 단일 문의 게시물 찾기
     *
     * @param boardId
     * @return
     */
    Optional<InquiryDto> getBoardById(Long boardId);

    /**
     * 게시물 삭제
     *
     * @param boardId
     */
    void deleteBoardById(Long boardId);
}
