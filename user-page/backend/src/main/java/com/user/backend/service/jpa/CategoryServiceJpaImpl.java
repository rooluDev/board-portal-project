package com.user.backend.service.jpa;

import com.user.backend.dto.CategoryDto;
import com.user.backend.repository.CategoryRepository;
import com.user.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Category Service Jpa Impl
 */
@Service("categoryJpa")
@RequiredArgsConstructor
@Transactional
public class CategoryServiceJpaImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    /**
     * boardType과 일치하는 카테고리 리스트 가져오기
     *
     * @param boardType 게시판 타입
     * @return 해당 게시판 타입의 카테고리 리스트
     */
    @Override
    public List<CategoryDto> getCategoryListByBoardType(String boardType) {
        return categoryRepository.findByBoardType(boardType)
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
    }
}
