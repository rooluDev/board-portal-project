package com.user.backend.service;

import com.user.backend.dto.CommentDto;
import com.user.backend.mapper.CommentMapper;
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

    @Override
    public List<CommentDto> getCommentListByBoardId(Long boardId, String boardType) {
        return commentMapper.selectCommentListByBoardId(boardId, boardType);
    }

    @Override
    public void deleteCommentById(Long commentId) {
        commentMapper.deleteCommentById(commentId);
    }

    @Override
    public Long addComment(CommentDto commentDto) {
        commentMapper.insertComment(commentDto);
        return commentDto.getCommentId();
    }
}
