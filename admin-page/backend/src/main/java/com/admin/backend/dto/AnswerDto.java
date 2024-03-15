package com.admin.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Answer Dto
 */
@Getter
@Setter
@Builder
public class AnswerDto {
    private Long answerId;
    private Long boardId;
    private String authorId;
    private String content;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
