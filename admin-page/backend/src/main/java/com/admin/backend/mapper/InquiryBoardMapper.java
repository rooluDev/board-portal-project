package com.admin.backend.mapper;

import com.admin.backend.dto.InquiryDto;
import com.admin.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * tb_inquiry board mapper
 */
@Mapper
public interface InquiryBoardMapper {

    /**
     * 검색조건과 페이지네이션에 맞는 문의 게시물 리스트 SELECT
     *
     * @param searchConditionDto
     * @return
     */
    List<InquiryDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * 검색조건에 맞는 문의 게시물 총 개수 SELECT
     *
     * @param searchConditionDto
     * @return
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * boardId(pk)로 board SELECT
     *
     * @param boardId
     * @return
     */
    Optional<InquiryDto> selectBoardById(Long boardId);

    /**
     * DELETE Board By Id
     *
     * @param boardId
     */
    void deleteBoardById(Long boardId);
}
