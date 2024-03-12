package com.admin.backend.dto;

import lombok.*;

import java.sql.Timestamp;

/**
 * Free Board Dto
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeBoardDto {
    private Long boardId;
    private Long categoryId;
    private String authorType;
    private String authorId;
    private String title;
    private String content;
    private int views;
    private Timestamp createdAt;
    private Timestamp editedAt;
    private String categoryName; // list page에 필요한 카테고리 이름
    private Long fileId; // list page에 필요한 파일 여부를 위한 파일 아이디
    private String adminName; // list page에 필요한 등록자 이름
    private String memberName; // list page에 필요한 등록자 이름
}
