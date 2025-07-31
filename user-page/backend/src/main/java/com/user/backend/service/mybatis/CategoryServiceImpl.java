package com.user.backend.service.mybatis;

import com.user.backend.dto.CategoryDto;
import com.user.backend.mapper.CategoryMapper;
import com.user.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Category Service 구현체
 */
@Service("categoryMybatis")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategoryListByBoardType(String boardType) {
        return categoryMapper.selectCategoryByBoardType(boardType);
    }
}
