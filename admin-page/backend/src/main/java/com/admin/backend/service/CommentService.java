package com.admin.backend.service;

import com.admin.backend.dto.CommentDto;

import java.util.List;
import java.util.Optional;

/**
 * Comment Service Interface
 */
public interface CommentService {

    /**
     * boardType에 맞는 boardId에 있는 댓글 리스트 가져오기
     *
     * @param boardId
     * @param boardType
     * @return
     */
    List<CommentDto> getCommentListByBoardId(Long boardId, String boardType);

    /**
     * comment 삭제
     *
     * @param commentId
     */
    void deleteCommentById(Long commentId);

    /**
     * comment 등록
     *
     * @param commentDto
     */
    Long addComment(CommentDto commentDto);

    /**
     * comment 가져오기
     *
     * @param commentId
     * @return
     */
    Optional<CommentDto> getCommentById(Long commentId);
}
