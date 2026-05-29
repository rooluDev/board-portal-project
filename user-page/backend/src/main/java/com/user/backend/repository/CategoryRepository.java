package com.user.backend.repository;

import com.user.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Category Repository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * boardType과 일치하는 카테고리 리스트 조회
     *
     * @param boardType 게시판 타입
     * @return 해당 게시판 타입의 카테고리 리스트
     */
    List<Category> findByBoardType(String boardType);
}