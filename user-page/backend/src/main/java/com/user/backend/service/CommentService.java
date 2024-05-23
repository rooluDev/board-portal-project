package com.user.backend.service;

import com.user.backend.dto.CommentDto;

import java.util.List;
import java.util.Optional;

/**
 * Comment Service Interface
 */
public interface CommentService {

    /**
     * 특정 게시물에 있는 댓글 리스트 가져오기
     *
     * @param boardId   boardId
     * @param boardType boardType
     * @return boardType에 boardId의 등록된 댓글 리스트
     */
    List<CommentDto> getCommentListByBoardId(Long boardId, String boardType);

    /**
     * comment 삭제
     *
     * @param commentId ( pk )
     */
    void deleteCommentById(Long commentId);

    /**
     * comment 등록
     *
     * @param commentDto ( content, board_type, board_id, author_type, author_id )
     */
    Long addComment(CommentDto commentDto);
}
