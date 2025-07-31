package com.user.backend.repository;

import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.NoticeBoard;
import com.user.backend.specification.NoticeBoardSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * NoticeBoardRepository
 */
@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> , JpaSpecificationExecutor<NoticeBoard> {


    default long findTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        Specification<NoticeBoard> spec = NoticeBoardSpecification.findBySearchCondition(searchConditionDto);
        return count(spec);
    }

    default Page<NoticeBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {
        Specification<NoticeBoard> spec = NoticeBoardSpecification.findBySearchCondition(searchConditionDto);
        Sort.Direction dir = Sort.Direction.fromString(searchConditionDto.getOrderDirection());
        String orderProp = searchConditionDto.getOrderValue() != null ? searchConditionDto.getOrderValue() : "createdAt";
        Pageable pageable = PageRequest.of(searchConditionDto.getPageNum() - 1, searchConditionDto.getPageSize(), dir, orderProp);
        return findAll(spec, pageable);
    }

    /**
     * 메인 페이지 최신 6건 조회
     */
    List<NoticeBoard> findTop6ByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = {"category","author"})
    List<NoticeBoard> findByIsFixedTrueOrderByCreatedAtDesc();

}
