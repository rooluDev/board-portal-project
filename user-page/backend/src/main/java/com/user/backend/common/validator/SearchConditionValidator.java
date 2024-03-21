package com.user.backend.common.validator;

import com.admin.backend.common.exception.IllegalSearchConditionDataException;
import com.user.backend.dto.SearchConditionDto;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Search Condition Validator
 */
@Slf4j
public class SearchConditionValidator {

    /**
     * Search Condition Validator
     *
     * @param searchConditionDto 검색조건
     */
    public static void validateSearchCondition(SearchConditionDto searchConditionDto) {
        validateDate(searchConditionDto.getStartDate(), searchConditionDto.getEndDate());
    }

    private static void validateDate(String startDate, String endDate) {
        LocalDate localDateStart = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate localDateEnd = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        long difference = ChronoUnit.MONTHS.between(localDateStart, localDateEnd);

        if (difference > 12) {
            throw new IllegalSearchConditionDataException("최대 검색 기간은 1년입니다.");
        }
    }
}
