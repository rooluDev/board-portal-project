package com.user.backend.repository;

import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.GalleryBoard;
import com.user.backend.specification.GalleryBoardSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Gallery Board Repository
 */
@Repository
public interface GalleryBoardRepository extends JpaRepository<GalleryBoard,Long> , JpaSpecificationExecutor<GalleryBoard> {

    default long findTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        Specification<GalleryBoard> specification = GalleryBoardSpecification.findBySearchCondition(searchConditionDto);
        return count(specification);
    }

    default Page<GalleryBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {
        Specification<GalleryBoard> specification = GalleryBoardSpecification.findBySearchCondition(searchConditionDto);
        Sort.Direction direction = Sort.Direction.fromString(searchConditionDto.getOrderDirection());
        String orderValue = searchConditionDto.getOrderValue() != null ? searchConditionDto.getOrderValue() : "createdAt";
        Pageable pageable = PageRequest.of(searchConditionDto.getPageNum() - 1, searchConditionDto.getPageSize(), direction, orderValue);
        return findAll(specification, pageable);
    }

    /**
     * MyBatis: selectBoardListForMain
     * 삭제되지 않은 최신 게시물 상위 6건 조회
     */
    List<GalleryBoard> findTop6ByIsDeletedFalseOrderByCreatedAtDesc();

    /**
     * MyBatis: selectBoardByIdAndMemberId
     * 게시물 ID와 작성자 ID가 일치하는 게시물 조회
     */
    Optional<GalleryBoard> findByBoardIdAndAuthorId(Long boardId, String authorId);
}
