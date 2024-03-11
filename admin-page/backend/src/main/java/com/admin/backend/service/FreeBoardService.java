package com.admin.backend.service;

import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.dto.SearchConditionDto;

import java.util.List;

/**
 * Free Board Service Interface
 */
public interface FreeBoardService {

    /**
     * 검색조건에 맞는 자유게시물의 총 개수 가져오기
     *
     * @param searchConditionDto
     * @return
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 검색조건과 페이지네이션에 맞는 자유게시물 리스트 가져오기
     *
     * @param searchConditionDto
     * @return
     */
    List<FreeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto);
}
