package com.admin.backend.service;

import com.admin.backend.dto.CategoryDto;
import com.admin.backend.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category Service 구현체
 */
@Service
@Primary
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryMapper categoryMapper;

    /**
     * boardType과 일치하는 카테고리 리스트 가져오기
     *
     * @param boardType 게시판 타입
     * @return boardType과 일치하는 카테고리 리스트
     */
    @Override
    public List<CategoryDto> getCategoryListByBoardType(String boardType) {
        return categoryMapper.selectCategoryByBoardType(boardType);
    }
}
