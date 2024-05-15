package com.user.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

/**
 * Inquiry Dto
 */
@Getter
@Setter
public class InquiryBoardDto {
    private Long boardId;
    private String authorId;
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 99, message = "100자 미만으로 작성하세요")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 3999, message = "내용이 4000자 미만으로 작성하세요.")
    private String content;
    private int views;
    private String isSecret;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String authorName; // 리스트 페이지에 필요한 등록자 이름
    private String answerId; // 리스트 페이지에 필요한 답변완료 옵션
}
