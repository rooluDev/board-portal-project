package com.admin.backend.service;

import com.admin.backend.common.exception.FixedBoardFullException;
import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;

import java.util.List;
import java.util.Optional;

/**
 * Notice Board Service Interface
 */
public interface NoticeBoardService {

    /**
     * 검색조건과 페이지네이션에 맞는 공지사항 리스트 가져오기
     *
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
     *
     * @param searchConditionDto
     * @return
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 공지사항 추가
     *
     * @param noticeBoardDto
     */
    void addBoard(NoticeBoardDto noticeBoardDto) throws FixedBoardFullException;

    /**
     * boardId(pk)로 notice board 가져오기
     *
     * @param boardId
     * @return
     */
    Optional<NoticeBoardDto> getBoardByBoardId(Long boardId);

    /**
     * board 수정
     *
     * @param noticeBoardDto
     */
    void modifyBoard(NoticeBoardDto noticeBoardDto);

    /**
     * board 삭제
     *
     * @param boardId
     */
    void deleteBoardByBoardId(Long boardId);

    /**
     * 조회수 1 증가
     *
     * @param boardId
     */
    void increaseView(Long boardId);
}
