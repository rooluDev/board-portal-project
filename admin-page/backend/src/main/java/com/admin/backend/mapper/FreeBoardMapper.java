package com.admin.backend.mapper;

import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * tb_free_board DB mapper
 */
@Mapper
public interface FreeBoardMapper {

    /**
     * 검색조건에 맞는 자유게시물의 총 개수 SELECT
     *
     * @param searchConditionDto
     * @return
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * 검색조건과 페이지네이션에 맞는 자유게시물 리스트 SELECT
     *
     * @param searchConditionDto
     * @return
     */
    List<FreeBoardDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 자유 게시물 추가 INSERT
     *
     * @param freeBoardDto
     */
    void insertBoard(FreeBoardDto freeBoardDto);

    /**
     * 단일 자유 게시물 찾기 SELECT
     *
     * @param boardId
     * @return
     */
    Optional<FreeBoardDto> selectBoardById(Long boardId);

    /**
     * content = 삭제된 게시판입니다.
     *
     * @param boardId
     */
    void updateBoardById(Long boardId);

    /**
     * view + 1
     *
     * @param boardId
     */
    void updateView(Long boardId);

    /**
     * UPDATE tb_free_board
     *
     * @param freeBoardDto ( categoryId, title, content, boardId )
     */
    void updateBoard(FreeBoardDto freeBoardDto);
}
