package com.user.backend.service;

import com.user.backend.dto.GalleryBoardDto;
import com.user.backend.dto.SearchConditionDto;

import java.util.List;
import java.util.Optional;

/**
 * Gallery Board Service Interface
 */
public interface GalleryBoardService {

    /**
     * 검색조건에 맞는 갤러리 게시물의 총 개수 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 갤러리 게시물의 총 개수
     */
    int getTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트 가져오기
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트
     */
    List<GalleryBoardDto> getBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 게시물 추가
     *
     * @param galleryBoardDto ( category_id, author_type, author_id, title, content )
     * @return 생성된 pk
     */
    Long addBoard(GalleryBoardDto galleryBoardDto);

    /**
     * 게시물 찾기
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 갤러리 게시물
     */
    Optional<GalleryBoardDto> getBoardById(Long boardId);

    /**
     * 게시물 삭제
     *
     * @param boardId ( pk )
     */
    void deleteBoard(Long boardId);

    /**
     * 조회수 증가
     *
     * @param boardId ( pk )
     */
    void increaseView(Long boardId);

    /**
     * 게시물 수정
     *
     * @param galleryBoardDto ( categoryId, title, content, boardId )
     */
    void modifyBoard(GalleryBoardDto galleryBoardDto);
}
