package com.admin.backend.dto;

import lombok.*;

import java.sql.Timestamp;

/**
 * Answer Dto
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDto {
    private Long answerId;
    private Long boardId;
    private String authorId;
    private String content;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
