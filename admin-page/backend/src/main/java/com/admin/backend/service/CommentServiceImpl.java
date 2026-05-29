package com.admin.backend.service;

import com.admin.backend.dto.CommentDto;
import com.admin.backend.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Comment Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    /**
     * 특정 게시물에 있는 댓글 리스트 가져오기
     *
     * @param boardId   게시물의 pk
     * @param boardType 게시판 타입
     * @return boardType에 boardId의 등록된 댓글 리스트
     */
    @Override
    public List<CommentDto> getCommentListByBoardId(Long boardId, String boardType) {
        return commentMapper.selectCommentListByBoardId(boardId, boardType);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 댓글의 pk
     */
    @Override
    public void deleteCommentById(Long commentId) {
        commentMapper.deleteCommentById(commentId);
    }

    /**
     * 댓글 등록
     *
     * @param commentDto 등록할 댓글 데이터 (content, boardType, boardId, authorType, authorId)
     * @return 생성된 댓글의 pk
     */
    @Override
    public Long addComment(CommentDto commentDto) {
        commentMapper.insertComment(commentDto);
        return commentDto.getCommentId();
    }

    /**
     * 댓글 가져오기
     *
     * @param commentId 댓글의 pk
     * @return commentId와 일치하는 댓글
     */
    @Override
    public Optional<CommentDto> getCommentById(Long commentId) {
        return commentMapper.selectCommentById(commentId);
    }
}
