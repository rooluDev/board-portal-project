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
}
