package com.admin.backend.mapper;

import com.admin.backend.dto.FreeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
     * 자유 게시물 추가
     *
     * @param freeBoardDto
     */
    void insertBoard(FreeBoardDto freeBoardDto);
}
