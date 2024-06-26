package com.admin.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Comment Dto
 */
@Getter
@Setter
public class CommentDto {
    private Long commentId;
    @NotBlank
    private String content;
    private String boardType;
    private Long boardId;
    private String authorType;
    private String authorId;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String adminName; // 댓글에 필요한 authorName
    private String memberName; // 댓글에 필요한 authorName
}
