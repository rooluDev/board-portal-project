package com.admin.backend.mapper;


import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * tb_notice_board DB Mapper
 */
@Mapper
public interface NoticeBoardMapper {

    /**
     * 검색조건과 페이지네이션에 맞는 공지사항 리스트 SELECT
     * @param searchConditionDto
     * @return
     */
    List<NoticeBoardDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 상단 고정인 공지사항 리스트 SELECT
     * @return
     */
    List<NoticeBoardDto> selectFixedBoardList();

    /**
     * 검색조건에 맞는 공지사항의 총 개수 SELECT
     * @param searchConditionDto
     * @return
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

}
