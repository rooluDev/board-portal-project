package com.user.backend.dto;

import lombok.*;

import java.sql.Timestamp;

/**
 * Inquiry Dto
 */
@Getter
@Setter
public class InquiryDto {
    private Long boardId;
    private String authorId;
    private String title;
    private String content;
    private int views;
    private String secret;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String memberName; // 리스트 페이지에 필요한 등록자 이름
    private String answerId; // 리스트 페이지에 필요한 답변완료 옵션
}
