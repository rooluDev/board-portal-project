package com.user.backend.specification;

import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.InquiryBoard;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * InquiryBoardSpecification
 */
public class InquiryBoardSpecification {

    /**
     * 검색조건을 통한 쿼리
     *
     * @param searchConditionDto 검색조건
     * @param memberId 멤버 ID
     * @return 쿼리
     */
    public static Specification<InquiryBoard> findBySearchCondition(SearchConditionDto searchConditionDto, String memberId) {
        return (root, query, criteriaBuilder) -> {
            // 조건들을 저장할 리스트
            List<Predicate> predicates = new ArrayList<>();

            // 1) 날짜 범위 검색
            if (searchConditionDto.getStartDate() != null && searchConditionDto.getEndDate() != null) {
                Timestamp start = searchConditionDto.getStartDateTimestamp();
                Timestamp end = searchConditionDto.getEndDateTimestamp();
                predicates.add(
                        criteriaBuilder.between(root.get("createdAt"), start, end)
                );
            }

            // 2) 키워드 검색: 제목 OR 내용
            if (searchConditionDto.getSearchText() != null && !searchConditionDto.getSearchText().isBlank()) {
                String pattern = "%" + searchConditionDto.getSearchText().trim() + "%";
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("title"), pattern),
                                criteriaBuilder.like(root.get("content"), pattern)
                        )
                );
            }

            // 3) 본인 작성 필터
            if (memberId != null && !memberId.isEmpty()) {
                predicates.add(
                        criteriaBuilder.equal(root.get("authorId"), memberId)
                );
            }

            // AND 조건으로 결합
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
