package com.user.backend.dto;

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
    private String authorName; // 패아자 답변에 필요한 등록자 이름
}
