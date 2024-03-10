package com.admin.backend.mapper;


import com.admin.backend.dto.CategoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * tb_category DB Mapper
 */
@Mapper
public interface CategoryMapper {

    /**
     * boardType에 맞는 카테고리 리스트 SELECT
     * @param boardType
     * @return
     */
    List<CategoryDto> selectCategoryByBoardType(String boardType);
}
