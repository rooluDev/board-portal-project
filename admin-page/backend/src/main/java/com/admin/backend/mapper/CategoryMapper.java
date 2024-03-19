package com.admin.backend.mapper;


import com.admin.backend.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * tb_category Mapper
 */
@Mapper
public interface CategoryMapper {

    /**
     * SELECT tb_category By boardType
     *
     * @param boardType boardType
     * @return boardType에 맞는 카테고리 리스트
     */
    List<CategoryDto> selectCategoryByBoardType(String boardType);
}
