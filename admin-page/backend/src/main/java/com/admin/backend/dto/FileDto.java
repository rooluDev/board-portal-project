package com.admin.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * File Dto
 */
@Getter
@Setter
@Builder
public class FileDto {
    private Long fileId;
    private String boardType;
    private Long boardId;
    private String originalName;
    private String physicalName;
    private String filePath;
    private String extension;
    private Long size;
    private int thumbnail;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
