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

    /**
     * boardType과 일치하는 카테고리 리스트 가져오기
     *
     * @param boardType 게시판 타입
     * @return 해당 게시판 타입의 카테고리 리스트
     */
    @Override
    public List<CategoryDto> getCategoryListByBoardType(String boardType) {
        return categoryMapper.selectCategoryByBoardType(boardType);
    }
}
