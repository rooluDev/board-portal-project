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


    /**
     * 동적 검색 조건에 맞는 공지사항 총 개수 조회
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 공지사항 총 개수
     */
    default long findTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        Specification<NoticeBoard> spec = NoticeBoardSpecification.findBySearchCondition(searchConditionDto);
        return count(spec);
    }

    /**
     * 동적 검색 조건에 맞는 공지사항 목록 조회 (페이징 및 정렬)
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 공지사항 Page
     */
    default Page<NoticeBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {
        Specification<NoticeBoard> spec = NoticeBoardSpecification.findBySearchCondition(searchConditionDto);
        Sort.Direction dir = Sort.Direction.fromString(searchConditionDto.getOrderDirection());
        String orderProp = searchConditionDto.getOrderValue() != null ? searchConditionDto.getOrderValue() : "createdAt";
        Pageable pageable = PageRequest.of(searchConditionDto.getPageNum() - 1, searchConditionDto.getPageSize(), dir, orderProp);
        return findAll(spec, pageable);
    }

    /**
     * 메인 페이지 최신 공지사항 6건 조회
     *
     * @return 최신 공지사항 6건
     */
    List<NoticeBoard> findTop6ByOrderByCreatedAtDesc();

    /**
     * 상단 고정인 공지사항 목록 조회 (category, author 페치 조인)
     *
     * @return 상단 고정인 공지사항 리스트 (생성일 내림차순)
     */
    @EntityGraph(attributePaths = {"category","author"})
    List<NoticeBoard> findByIsFixedTrueOrderByCreatedAtDesc();

}
