package com.admin.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "제목을 입력하세요.")
    @Size(max = 99, message = "제목을 100자 미만으로 작성하세요")
    private String title;
    @NotBlank(message = "내용을 입력하세요.")
    @Size(max = 3999, message = "내용을 4000자 미만으로 작성하세요.")
    private String content;
    private int views;
    private String isFixed;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String authorName; // 리스트 페이지에 필요한 등록자 이름
    private String categoryName; // 리스트 페이지에 필요한 카테고리 이름
}
