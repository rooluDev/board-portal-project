package com.admin.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
public class ThumbnailDto {
    private Long thumbnailId;
    private Long fileId;
    private String originalName;
    private String physicalName;
    private String filePath;
    private String extension;
    private Long size;
    private Timestamp createdAt;
    private Timestamp editedAt;
}
