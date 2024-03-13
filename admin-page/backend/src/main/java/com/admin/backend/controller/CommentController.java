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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 삭제
     *
     * @param commentId
     * @return
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Long> deleteComment(@PathVariable(name = "commentId") Long commentId) {

        commentService.deleteCommentById(commentId);

        return ResponseEntity.ok().body(commentId);
    }

    /**
     * 댓글 등록
     *
     * @param commentDto
     * @param adminDto
     * @return
     */
    @PostMapping("/comment")
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto,
                                                 @SessionAttribute(name = LoginController.ADMIN_SESSION_ID) AdminDto adminDto) {

        commentDto.setAuthorId(adminDto.getAdminId());
        commentDto.setAuthorType(Author.ADMIN.getAuthorType());
        Long commentId = commentService.addComment(commentDto);

        CommentDto comment = commentService.getCommentById(commentId).orElseThrow(() -> new CommentNotFoundException("잘못된 요청입니다."));


        return ResponseEntity.ok().body(comment);
    }
}
