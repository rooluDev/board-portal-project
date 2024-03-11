package com.admin.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * Notice Board Dto
 */
@Getter
@Setter
public class NoticeBoardDto {
    private Long boardId;
    private Long categoryId;
    private String authorId;
    private String title;
    private String content;
    private int views;
    private String fixed;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String authorName; // 리스트 페이지에 필요한 등록자 이름
    private String categoryName; // 리스트 페이지에 필요한 카테고리 이름
}
