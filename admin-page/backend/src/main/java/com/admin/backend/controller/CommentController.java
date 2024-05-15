package com.admin.backend.controller;

import com.admin.backend.common.exception.CommentNotFoundException;
import com.admin.backend.common.type.Author;
import com.admin.backend.dto.AdminDto;
import com.admin.backend.dto.CommentDto;
import com.admin.backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Comment Controller
 */
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     *
     * @param commentDto comment form data ( boardId, content, boardType )
     * @param adminDto   저장된 세션 정보 ( table column에 저장될 데이터 )
     * @return 추가된 comment
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto,
                                                 @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto) {

        // author 세팅
        commentDto.setAuthorId(adminDto.getAdminId());
        commentDto.setAuthorType(Author.ADMIN.getAuthorType());

        // 댓글 추가 후 생성 된 pk return
        Long commentId = commentService.addComment(commentDto);

        // dom 추가를 위한 추가 한 comment 가져오기
        CommentDto comment = commentService.getCommentById(commentId).orElseGet(CommentDto::new);

        return ResponseEntity.ok().body(comment);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId PathVariable
     * @return commentId ( pk )
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Long> deleteComment(@PathVariable(name = "commentId") Long commentId) {

        commentService.getCommentById(commentId).orElseThrow(() -> new CommentNotFoundException("잘못된 요청입니다."));

        commentService.deleteCommentById(commentId);

        return ResponseEntity.ok().body(commentId);
    }
}
