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

    @Override
    public List<CategoryDto> getCategoryListByBoardType(String boardType) {
        return categoryRepository.findByBoardType(boardType)
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();
    }
}
