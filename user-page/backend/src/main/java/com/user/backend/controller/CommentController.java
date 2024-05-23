package com.user.backend.controller;

import com.user.backend.common.type.Author;
import com.user.backend.dto.CommentDto;
import com.user.backend.service.CommentService;
import com.user.backend.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Comment Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final JwtService jwtService;

    /**
     * 댓글 등록
     *
     * @param commentDto 등록할 Comment
     * @param request    HttpServletRequest
     * @return 등록 후 게시물의 등록된 commentList
     */
    @PostMapping("/comment")
    public ResponseEntity<List<CommentDto>> addComment(@Valid @RequestBody CommentDto commentDto, HttpServletRequest request) {
        // author 세팅
        String memberId = jwtService.getMemberIdFromToken(request);
        commentDto.setAuthorType(Author.MEMBER.getAuthorType());
        commentDto.setAuthorId(memberId);

        // 추가
        commentService.addComment(commentDto);

        // 댓글 리스트 리로드
        List<CommentDto> commentDtoList = commentService.getCommentListByBoardId(commentDto.getBoardId(), commentDto.getBoardType());

        return ResponseEntity.ok().body(commentDtoList);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId PathVariable ( pk )
     * @param request   HttpServletRequest
     * @return null
     */
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity deleteComment(@PathVariable(name = "commentId") Long commentId, HttpServletRequest request) {

        // 로그인 확인
        jwtService.getMemberIdFromToken(request);

        // 삭제
        commentService.deleteCommentById(commentId);

        return ResponseEntity.ok().build();
    }

}
