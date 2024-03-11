package com.admin.backend.service;

import com.admin.backend.common.exception.FixedBoardFullException;
import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;

import java.util.List;

/**
 * Notice Board Service Interface
 */
public interface NoticeBoardService {

    /**
     * 검색조건과 페이지네이션에 맞는 공지사항 리스트 가져오기
     * @param searchConditionDto
     * @return
     */
    List<NoticeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 상단 고정인 공지사항 리스트 가져오기
     *
     * @return
     */
    List<NoticeBoardDto> getFixedBoardList();

    /**
     * 검색조건에 맞는 공지사항의 총 개수 가져오기
     * @param searchConditionDto
     * @return
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 공지사항 추가
     * @param noticeBoardDto
     */
    void addBoard(NoticeBoardDto noticeBoardDto) throws FixedBoardFullException;

}
