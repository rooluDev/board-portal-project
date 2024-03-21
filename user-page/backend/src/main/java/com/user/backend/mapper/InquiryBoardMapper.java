package com.user.backend.mapper;

import com.user.backend.dto.InquiryDto;
import com.user.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * tb_inquiry_board mapper
 */
@Mapper
public interface InquiryBoardMapper {

    /**
     * SELECT tb_inquiry_board By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 리스트
     */
    List<InquiryDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * SELECT totalRowCount By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건 맞는 문의 게시물 리스트 의 수
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * SELECT tb_inquiry_board By boardId
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 tb_inquiry_board
     */
    Optional<InquiryDto> selectBoardById(Long boardId);

    /**
     * DELETE tb_inquiry_board By Id
     *
     * @param boardId ( pk )
     */
    void deleteBoardById(Long boardId);

    /**
     * UPDATE tb_inquiry_board
     * SET views = views + 1
     *
     * @param boardId ( pk )
     */
    void updateViewById(Long boardId);
}
