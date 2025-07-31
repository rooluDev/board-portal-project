package com.user.backend.controller;

import com.user.backend.dto.CategoryDto;
import com.user.backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Category Controller
 */
@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(@Qualifier("categoryJpa") CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * board 타입에 맞는 등록된 카테고리 리스트 반환
     *
     * @param boardType board 타입
     * @return categoryDtoList
     */
    @GetMapping("/categories")
    public ResponseEntity getCategoryList(@RequestParam(name = "boardType") String boardType) {

        List<CategoryDto> categoryDtoList = categoryService.getCategoryListByBoardType(boardType);

        return ResponseEntity.ok(categoryDtoList);
    }
}
