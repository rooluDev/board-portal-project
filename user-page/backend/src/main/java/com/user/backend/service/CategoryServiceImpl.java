package com.user.backend.service;

import com.user.backend.dto.CategoryDto;
import com.user.backend.mapper.CategoryMapper;
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
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategoryListByBoardType(String boardType) {
        return categoryMapper.selectCategoryByBoardType(boardType);
    }
}
