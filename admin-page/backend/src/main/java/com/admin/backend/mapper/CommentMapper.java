package com.admin.backend.mapper;

import com.admin.backend.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * tb_comment DB Mapper
 */
@Mapper
public interface CommentMapper {

    /**
     * tb_comment DB SELECT By BoardId And BoardType
     *
     * @param boardId
     * @param boardType
     * @return
     */
    List<CommentDto> selectCommentListByBoardId(@Param("boardId") Long boardId, @Param("boardType") String boardType);

    /**
     * DELETE tb_comment By commentId
     *
     * @param commentId
     */
    void deleteCommentById(Long commentId);

    /**
     * INSERT tb_comment
     *
     * @param commentDto
     */
    void insertComment(CommentDto commentDto);

    /**
     * SELECT tb_comment By Id
     *
     * @param commentId
     * @return
     */
    Optional<CommentDto> selectCommentById(Long commentId);
}
