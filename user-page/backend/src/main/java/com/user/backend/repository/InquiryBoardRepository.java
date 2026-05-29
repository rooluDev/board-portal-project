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
     *
     * @param searchConditionDto 검색조건
     * @param memberId           나의 문의 내역 조회 시 사용할 회원 ID (null이면 전체 조회)
     * @return 검색조건에 맞는 문의 게시물 총 개수
     */
    default long findTotalRowCountByCondition(SearchConditionDto searchConditionDto, String memberId) {
        return count(InquiryBoardSpecification.findBySearchCondition(searchConditionDto, memberId));
    }

    /**
     * 동적 검색 조건에 맞는 게시물 목록 조회 (페이징 및 정렬)
     *
     * @param cond     검색조건
     * @param memberId 나의 문의 내역 조회 시 사용할 회원 ID (null이면 전체 조회)
     * @return 검색조건과 페이지네이션에 맞는 문의 게시물 Page
     */
    default Page<InquiryBoard> findByCondition(SearchConditionDto cond, String memberId) {
        Sort.Direction dir = Sort.Direction.fromString(cond.getOrderDirection());
        String orderProp = cond.getOrderValue() != null ? cond.getOrderValue() : "createdAt";
        Pageable pageable = PageRequest.of(cond.getPageNum() - 1, cond.getPageSize(), dir, orderProp);
        return findAll(InquiryBoardSpecification.findBySearchCondition(cond, memberId), pageable);
    }

    /**
     * 메인 페이지용 최신 문의 게시물 6건 조회
     *
     * @return 최신 문의 게시물 6건
     */
    List<InquiryBoard> findTop6ByOrderByCreatedAtDesc();

    /**
     * 게시물 ID와 작성자 회원 ID가 일치하는 게시물 조회 (작성자 확인용)
     *
     * @param boardId  게시물 ID (pk)
     * @param authorId 작성자 회원 ID
     * @return boardId와 authorId가 일치하는 게시물 Optional
     */
    Optional<InquiryBoard> findByBoardIdAndAuthorMemberId(Long boardId, String authorId);
}
