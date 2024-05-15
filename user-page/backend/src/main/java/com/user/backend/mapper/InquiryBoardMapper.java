package com.user.backend.mapper;

import com.user.backend.dto.InquiryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    List<InquiryBoardDto> selectBoardListByCondition(@Param("searchCondition") SearchConditionDto searchConditionDto, @Param("memberId") String memberId);

    /**
     * SELECT totalRowCount By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건 맞는 문의 게시물 리스트 의 수
     */
    int selectTotalRowCountByCondition(@Param("searchCondition") SearchConditionDto searchConditionDto, @Param("memberId") String memberId);

    /**
     * SELECT tb_inquiry_board By boardId
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 tb_inquiry_board
     */
    Optional<InquiryBoardDto> selectBoardById(Long boardId);

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

    /**
     * SELECT tb_inquiry_board LIMIT 6
     *
     * @return 최신 게시물 6개
     */
    List<InquiryBoardDto> selectBoardListForMain();

    /**
     * INSERT tb_inquiry_board
     *
     * @param inquiryBoardDto ( author_id, title, content, is_secret )
     */
    void insertBoard(InquiryBoardDto inquiryBoardDto);

    /**
     * UPDATE tb_inquiry_board
     *
     * @param inquiryBoardDto ( title, content, is_secret )
     */
    void updateBoard(InquiryBoardDto inquiryBoardDto);

    /**
     * SELECT tb_inquiry_board By boardId And memberId
     *
     * @param boardId ( pk )
     * @param memberId authorId
     * @return boardId와 authorId가 일치하는 게시물
     */
    Optional<InquiryBoardDto> selectBoardByIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") String memberId);
}
