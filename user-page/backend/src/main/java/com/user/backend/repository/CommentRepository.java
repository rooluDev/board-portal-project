package com.user.backend.repository;

import com.user.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Comment Repository
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 게시판(boardType, boardId)에 등록된 댓글 전체 조회
     *
     * @param boardType 게시판 타입
     * @param boardId   게시물 ID
     * @return 해당 게시물의 댓글 리스트
     */
    List<Comment> findByBoardTypeAndBoardId(String boardType, Long boardId);

    /**
     * 특정 게시판(boardType, boardId)에 등록된 댓글 개수 조회
     *
     * @param boardType 게시판 타입
     * @param boardId   게시물 ID
     * @return 해당 게시물의 댓글 개수
     */
    long countByBoardTypeAndBoardId(String boardType, Long boardId);

}
