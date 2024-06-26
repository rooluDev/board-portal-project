package com.user.backend.mapper;

import com.user.backend.dto.GalleryBoardDto;
import com.user.backend.dto.SearchConditionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * tb_board_gallery mapper
 */
@Mapper
public interface GalleryBoardMapper {

    /**
     * SELECT totalRowCount By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건 맞는 갤러리 게시물 리스트 의 수
     */
    int selectTotalRowCountByCondition(SearchConditionDto searchConditionDto);

    /**
     * SELECT tb_gallery_board By searchCondition
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 갤러리 게시물 리스트
     */
    List<GalleryBoardDto> selectBoardListByCondition(SearchConditionDto searchConditionDto);

    /**
     * INSERT tb_gallery_board
     *
     * @param galleryBoardDto ( category_id, author_type, author_id, title, content )
     */
    void insertBoard(GalleryBoardDto galleryBoardDto);

    /**
     * SELECT tb_gallery_board By Id
     *
     * @param boardId ( pk )
     * @return boardId와 일치하는 tb_gallery_board
     */
    Optional<GalleryBoardDto> selectBoardById(Long boardId);

    /**
     * UPDATE tb_gallery_board
     * SET content = '삭제된 게시물입니다.', deleted = 1
     *
     * @param boardId ( pk )
     */
    void updateBoardByIdForDelete(Long boardId);

    /**
     * UPDATE tb_gallery_board
     * SET views = views + 1
     *
     * @param boardId ( pk )
     */
    void updateView(Long boardId);

    /**
     * UPDATE tb_gallery_board
     *
     * @param galleryBoardDto ( categoryId, title, content, boardId )
     */
    void updateBoard(GalleryBoardDto galleryBoardDto);

    /**
     * SELECT tb_gallery_board LIMIT 6
     *
     * @return 최신 게시물 6개
     */
    List<GalleryBoardDto> selectBoardListForMain();

    /**
     * SELECT tb_gallery_board By boardId And memberId
     *
     * @param boardId ( pk )
     * @param memberId authorId
     * @return boardId와 authorId가 일치하는 게시물
     */
    Optional<GalleryBoardDto> selectBoardByIdAndMemberId(@Param("boardId") Long boardId, @Param("memberId") String memberId);
}
