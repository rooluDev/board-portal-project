package com.admin.backend.mapper;


import com.admin.backend.dto.NoticeBoardDto;
import com.admin.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

/**
 * tb_notice_board Mapper
 */
@Mapper
public interface NoticeBoardMapper {

    /**
     * SELECT tb_notice_board By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 공지 게시물 리스트
     */
    List<NoticeBoardDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * SELECT tb_inquiry_board By fixed = 1
     *
     * @return 상단 고정인 공지 게시물 리스트
     */
    List<NoticeBoardDto> selectFixedBoardList();

    /**
     * SELECT totalRowCount By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건 맞는 공지 게시물 리스트 의 수
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * INSERT tb_notice_board
     *
     * @param noticeBoardDto ( category_id, author_id, title, content, fixed )
     */
    void insertBoard(NoticeBoardDto noticeBoardDto);

    /**
     * SELECT rowCount By fixed = 1
     *
     * @return 상단 고정인 공지사항 게시물의 수
     */
    int selectFixedBoardCount();

    /**
     * SELECT tb_notice_board By id
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 공지 게시물
     */
    Optional<NoticeBoardDto> selectBoardByBoardId(Long boardId);

    /**
     * UPDATE tb_notice_board
     *
     * @param noticeBoardDto ( categoryId, title, content, fixed )
     */
    void updateBoard(NoticeBoardDto noticeBoardDto);

    /**
     * DELETE tb_notice_board By id
     *
     * @param boardId ( pk )
     */
    void deleteBoardByBoardId(Long boardId);

    /**
     * UPDATE tb_notice_board
     * SET views = views + 1
     *
     * @param boardId ( pk )
     */
    void updateView(Long boardId);
}
