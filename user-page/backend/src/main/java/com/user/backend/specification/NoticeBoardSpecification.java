package com.user.backend.specification;

import com.user.backend.dto.SearchConditionDto;
import com.user.backend.entity.NoticeBoard;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * NoticeBoardSpecification
 */
public class NoticeBoardSpecification {

    /**
     * 검색조건을 통한 쿼리
     *
     * @param searchConditionDto 검색조건
     * @return 쿼리
     */
    public static Specification<NoticeBoard> findBySearchCondition(SearchConditionDto searchConditionDto) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchConditionDto.getStartDate() != null && searchConditionDto.getEndDate() != null) {
                predicates.add(
                        criteriaBuilder.between(
                                root.get("createdAt"),
                                searchConditionDto.getStartDateTimestamp(),
                                searchConditionDto.getEndDateTimestamp()
                        )
                );
            }

            if (searchConditionDto.getCategory() != null && searchConditionDto.getCategory() > 0) {
                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category").get("categoryId"),
                                searchConditionDto.getCategory()
                        )
                );
            }

            if (searchConditionDto.getSearchText() != null && !searchConditionDto.getSearchText().isBlank()) {
                String pattern = "%" + searchConditionDto.getSearchText().trim() + "%";
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(root.get("title"), pattern),
                                criteriaBuilder.like(root.get("content"), pattern)
                        )
                );
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
