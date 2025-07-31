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

    List<Category> findByBoardType(String boardType);
}