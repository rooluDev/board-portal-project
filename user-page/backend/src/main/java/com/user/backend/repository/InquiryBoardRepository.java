package com.user.backend.repository;

import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.InquiryBoard;
import com.user.backend.specification.InquiryBoardSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * InquiryBoardRepository
 */
@Repository
public interface InquiryBoardRepository extends JpaRepository<InquiryBoard, Long>, JpaSpecificationExecutor<InquiryBoard> {

    /**
     * 동적 검색 조건에 맞는 총 게시물 수 조회
     */
    default long findTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return count(InquiryBoardSpecification.findBySearchCondition(searchConditionDto, memberId));
    }

    /**
     * 동적 검색 조건에 맞는 게시물 목록 조회 (페이징 및 정렬)
     */
    default Page<InquiryBoard> findByCondition(SearchConditionDto cond, String memberId) {
        Sort.Direction dir = Sort.Direction.fromString(cond.getOrderDirection());
        String orderProp = cond.getOrderValue() != null ? cond.getOrderValue() : "createdAt";
        Pageable pageable = PageRequest.of(cond.getPageNum() - 1, cond.getPageSize(), dir, orderProp);
        return findAll(InquiryBoardSpecification.findBySearchCondition(cond, memberId), pageable);
    }

    List<InquiryBoard> findTop6ByOrderByCreatedAtDesc();

    Optional<InquiryBoard> findByBoardIdAndAuthorMemberId(Long boardId, String authorId);
}
