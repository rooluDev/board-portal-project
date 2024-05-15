package com.user.backend.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 페이지에서의 검색조건과 DB에서의 SELECT 조건
 */
@Getter
@Setter
@Slf4j
public class SearchConditionDto {
    private String startDate;
    private String endDate;
    private Long category;
    private String searchText;
    private int pageSize;
    private String orderValue;
    private String orderDirection;
    private int pageNum;
    private Timestamp startDateTimestamp;
    private Timestamp endDateTimestamp;
    private int offset;
    private String my;

    /**
     * offset을 computed로 사용자 정의 getter 생성
     *
     * @return Offset
     */
    public int getOffset() {
        return (this.pageNum - 1) * this.pageSize;
    }

    /**
     * Timestamp 사용자 정의 getter 생성
     *
     * @return timestamp
     */
    public Timestamp getStartDateTimestamp() {
        LocalDate localStateDate = LocalDate.parse(this.startDate);
        LocalDateTime startDate = localStateDate.atTime(LocalTime.MIN);
        return Timestamp.valueOf(startDate);
    }

    /**
     * Timestamp 사용자 정의 getter 생성
     *
     * @return timestamp
     */
    public Timestamp getEndDateTimestamp() {
        LocalDate localEndDate = LocalDate.parse(this.endDate);
        LocalDateTime endDate = localEndDate.atTime(LocalTime.MAX);
        return Timestamp.valueOf(endDate);
    }
}
