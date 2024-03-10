package com.admin.backend.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Category Dto
 */
@Getter
@Setter
public class CategoryDto {
    private Long categoryId;
    private String categoryName;
    private String boardType;
}
