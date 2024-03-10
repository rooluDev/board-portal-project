package com.admin.backend.service;

import com.admin.backend.dto.CategoryDto;

import java.util.List;

/**
 * Category Service Interface
 */
public interface CategoryService {

    /**
     * board type으로 카테고리 리스트 가져오기
     * @param boardType
     * @return
     */
    List<CategoryDto> getCategoryListByBoardType(String boardType);
}
