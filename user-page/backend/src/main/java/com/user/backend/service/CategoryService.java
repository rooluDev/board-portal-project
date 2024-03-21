package com.user.backend.service;

import com.user.backend.dto.CategoryDto;

import java.util.List;

/**
 * Category Service Interface
 */
public interface CategoryService {

    /**
     * boardType과 일치하는 카테고리 리스트 가져오기
     *
     * @param boardType boardType
     * @return boardType과 일치하는 카테고리 리스트
     */
    List<CategoryDto> getCategoryListByBoardType(String boardType);
}
