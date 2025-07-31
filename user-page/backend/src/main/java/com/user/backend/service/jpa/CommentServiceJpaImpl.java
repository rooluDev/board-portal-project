package com.user.backend.service.jpa;

import com.user.backend.dto.CommentDto;
import com.user.backend.entity.Comment;
import com.user.backend.repository.CommentRepository;
import com.user.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CommentServiceJpaImpl
 */
@Service("commentJpa")
@RequiredArgsConstructor
@Transactional
public class CommentServiceJpaImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CommentDto> getCommentListByBoardId(Long boardId, String boardType) {
        return commentRepository.findByBoardTypeAndBoardId(boardType,boardId)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();

    }

    @Override
    public void deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Long addComment(CommentDto commentDto) {
        Comment comment = commentRepository
                .save(modelMapper.map(commentDto, Comment.class));
        return comment.getCommentId();
    }
}
