package com.admin.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "답변을 입력하세요.")
    @Size(max = 3999, message = "답변을 4000자 미만으로 입력하세요.")
    private String content;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
