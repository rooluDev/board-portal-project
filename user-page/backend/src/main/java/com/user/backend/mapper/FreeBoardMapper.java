package com.user.backend.mapper;

import com.user.backend.dto.FreeBoardDto;
import com.user.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * tb_free_board mapper
 */
@Mapper
public interface FreeBoardMapper {

    /**
     * SELECT totalRowCount By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건 맞는 자유 게시물 리스트 의 수
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * SELECT tb_free_board By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 자유 게시물 리스트
     */
    List<FreeBoardDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * INSERT tb_free_board
     *
     * @param freeBoardDto ( category_id, author_type, author_id, title, content )
     */
    void insertBoard(FreeBoardDto freeBoardDto);

    /**
     * SELECT tb_free_board By Id
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 tb_free_board
     */
    Optional<FreeBoardDto> selectBoardById(Long boardId);

    /**
     * UPDATE tb_free_board
     * SET content = '삭제된 게시물입니다.', deleted = 1
     *
     * @param boardId ( pk )
     */
    void updateBoardByIdForDelete(Long boardId);

    /**
     * UPDATE tb_free_board
     * SET views = views + 1
     *
     * @param boardId ( pk )
     */
    void updateView(Long boardId);

    /**
     * UPDATE tb_free_board
     *
     * @param freeBoardDto ( categoryId, title, content, boardId )
     */
    void updateBoard(FreeBoardDto freeBoardDto);

    /**
     * SELECT tb_free_board LIMIT 6
     *
     * @return 최신 게시물 6개
     */
    List<FreeBoardDto> selectBoardListForMain();

    /**
     * SELECT tb_free_board By boardId And memberId
     *
     * @param boardId ( pk )
     * @param memberId authorId
     * @return boardId와 authorId가 일치하는 게시물
     */
    Optional<FreeBoardDto> selectBoardByIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") String memberId);
}
