package com.user.backend.service.mybatis;

import com.user.backend.dto.CommentDto;
import com.user.backend.mapper.CommentMapper;
import com.user.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Comment Service 구현체
 */
@Service("commentMybatis")
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper;

    /**
     * 특정 게시물에 있는 댓글 리스트 가져오기
     *
     * @param boardId   게시물 ID
     * @param boardType 게시판 타입
     * @return 해당 게시물의 댓글 리스트
     */
    @Override
    public List<CommentDto> getCommentListByBoardId(Long boardId, String boardType) {
        return commentMapper.selectCommentListByBoardId(boardId, boardType);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId 삭제할 댓글 ID (pk)
     */
    @Override
    public void deleteCommentById(Long commentId) {
        commentMapper.deleteCommentById(commentId);
    }

    /**
     * 댓글 등록
     *
     * @param commentDto 등록할 댓글 정보 (content, boardType, boardId, authorType, authorId)
     * @return 생성된 댓글의 ID
     */
    @Override
    public Long addComment(CommentDto commentDto) {
        commentMapper.insertComment(commentDto);
        return commentDto.getCommentId();
    }
}
