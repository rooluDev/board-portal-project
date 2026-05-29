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

    /**
     * 동적 검색 조건에 맞는 갤러리 게시물 총 개수 조회
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건에 맞는 갤러리 게시물 총 개수
     */
    default long findTotalRowCountByCondition(SearchConditionDto searchConditionDto) {
        Specification<GalleryBoard> specification = GalleryBoardSpecification.findBySearchCondition(searchConditionDto);
        return count(specification);
    }

    /**
     * 동적 검색 조건에 맞는 갤러리 게시물 목록 조회 (페이징 및 정렬)
     *
     * @param searchConditionDto 검색조건
     * @return 검색조건과 페이지네이션에 맞는 갤러리 게시물 Page
     */
    default Page<GalleryBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {
        Specification<GalleryBoard> specification = GalleryBoardSpecification.findBySearchCondition(searchConditionDto);
        Sort.Direction direction = Sort.Direction.fromString(searchConditionDto.getOrderDirection());
        String orderValue = searchConditionDto.getOrderValue() != null ? searchConditionDto.getOrderValue() : "createdAt";
        Pageable pageable = PageRequest.of(searchConditionDto.getPageNum() - 1, searchConditionDto.getPageSize(), direction, orderValue);
        return findAll(specification, pageable);
    }

    /**
     * 삭제되지 않은 최신 게시물 상위 3건 조회 (메인 페이지용)
     *
     * @return 삭제되지 않은 최신 갤러리 게시물 3건
     */
    List<GalleryBoard> findTop3ByIsDeletedFalseOrderByCreatedAtDesc();

    /**
     * 게시물 ID와 작성자 ID가 일치하는 게시물 조회 (작성자 확인용)
     *
     * @param boardId  게시물 ID (pk)
     * @param authorId 작성자 ID
     * @return boardId와 authorId가 일치하는 게시물 Optional
     */
    Optional<GalleryBoard> findByBoardIdAndAuthorId(Long boardId, String authorId);
}
