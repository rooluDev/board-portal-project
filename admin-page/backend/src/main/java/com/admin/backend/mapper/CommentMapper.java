package com.admin.backend.mapper;

import com.admin.backend.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * tb_comment Mapper
 */
@Mapper
public interface CommentMapper {

    /**
     * SELECT tb_comment By BoardId And BoardType
     *
     * @param boardId   boardId
     * @param boardType boardType
     * @return boardType에 boardId의 등록된 댓글 리스트
     */
    List<CommentDto> selectCommentListByBoardId(@Param("boardId") Long boardId, @Param("boardType") String boardType);

    /**
     * DELETE tb_comment By commentId
     *
     * @param commentId ( pk )
     */
    void deleteCommentById(Long commentId);

    /**
     * INSERT tb_comment
     *
     * @param commentDto ( content, board_type, board_id, author_type, author_id )
     */
    void insertComment(CommentDto commentDto);

    /**
     * SELECT tb_comment By Id
     *
     * @param commentId ( pk )
     * @return commentId와 일치하는 Comment
     */
    Optional<CommentDto> selectCommentById(Long commentId);
}
