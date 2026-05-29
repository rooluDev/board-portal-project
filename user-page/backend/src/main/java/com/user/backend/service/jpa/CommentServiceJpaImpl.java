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

    /**
     * 특정 게시물에 있는 댓글 리스트 가져오기
     *
     * @param boardId   게시물 ID
     * @param boardType 게시판 타입
     * @return 해당 게시물의 댓글 리스트
     */
    @Override
    public List<CommentDto> getCommentListByBoardId(Long boardId, String boardType) {
        return commentRepository.findByBoardTypeAndBoardId(boardType,boardId)
                .stream()
                .map(comment -> modelMapper.map(comment, CommentDto.class))
                .toList();

    }

    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID (pk)
     */
    @Override
    public void deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    /**
     * 댓글 등록
     *
     * @param commentDto 등록할 댓글 정보 (content, boardType, boardId, authorType, authorId)
     * @return 생성된 댓글의 ID
     */
    @Override
    public Long addComment(CommentDto commentDto) {
        Comment comment = commentRepository
                .save(modelMapper.map(commentDto, Comment.class));
        return comment.getCommentId();
    }
}
