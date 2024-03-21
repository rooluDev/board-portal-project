package com.user.backend.service;

import com.admin.backend.common.exception.FixedBoardFullException;
import com.user.backend.dto.NoticeBoardDto;
import com.user.backend.dto.SearchConditionDto;

import java.util.List;
import java.util.Optional;

/**
 * Notice Board Service Interface
 */
public interface NoticeBoardService {

    /**
     * 검색조건과 페이지네이션에 맞는 공지사항 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 공지사항 리스트
     */
    List<NoticeBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 상단 고정인 공지사항 리스트 가져오기
     *
     * @return 상단 고정인 공지사항 리스트
     */
    List<NoticeBoardDto> getFixedBoardList();

    /**
     * 검색조건에 맞는 공지사항의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 공지사항의 총 개수
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 공지사항 추가
     *
     * @param noticeBoardDto ( category_id, author_id, title, content, fixed )
     */
    void addBoard(NoticeBoardDto noticeBoardDto) throws FixedBoardFullException;

    /**
     * 공지사항 가져오기
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 공지사항
     */
    Optional<NoticeBoardDto> getBoardByBoardId(Long boardId);

    /**
     * 공지사항 수정
     *
     * @param noticeBoardDto ( categoryId, title, content, fixed )
     */
    void modifyBoard(NoticeBoardDto noticeBoardDto);

    /**
     * 공지사항 삭제
     *
     * @param boardId ( pk )
     */
    void deleteBoardByBoardId(Long boardId);

    /**
     * 조회수 증가
     *
     * @param boardId ( pk )
     */
    void increaseView(Long boardId);
}
