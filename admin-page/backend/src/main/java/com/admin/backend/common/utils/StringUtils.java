package com.admin.backend.common.utils;

import com.admin.backend.dto.SearchConditionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom String Utils
 */
public class StringUtils {

    /**
     * uri에 있는 숫자 덩어리를 추출하는 메소드
     *
     * @param uri uri
     * @return 추출된 numberList
     */
    public static List<String> extractNumberFromUri(String uri) {
        List<String> numberList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(uri);
        while (matcher.find()) {
            numberList.add(matcher.group());
        }
        return numberList;
    }

    /**
     * SearchCondition 객체를 카테고리가 있는 쿼리 스트링으로 파싱
     *
     * @param searchConditionDto 검색조건
     * @return 쿼리스트링
     */
    public static String searchConditionToQueryStringWithCategory(SearchConditionDto searchConditionDto) {
        String queryString = "?startDate=" + searchConditionDto.getStartDate() + "&endDate=" + searchConditionDto.getEndDate() +
                "&category=" + searchConditionDto.getCategory() + "&searchText=" + searchConditionDto.getSearchText() +
                "&pageSize=" + searchConditionDto.getPageSize() + "&orderValue=" + searchConditionDto.getOrderValue() +
                "&orderDirection=" + searchConditionDto.getOrderDirection() + "&pageNum=" + searchConditionDto.getPageNum();
        return queryString;
    }

    /**
     * SearchCondition 객체를 카테고리가 없는 쿼리 스트링으로 파싱
     *
     * @param searchConditionDto 검색조건
     * @return 쿼리스트링
     */
    public static String searchConditionToQueryStringWithOutCategory(SearchConditionDto searchConditionDto) {
        String queryString = "?startDate=" + searchConditionDto.getStartDate() + "&endDate=" + searchConditionDto.getEndDate() +
                "&searchText=" + searchConditionDto.getSearchText() + "&pageSize=" + searchConditionDto.getPageSize() +
                "&orderValue=" + searchConditionDto.getOrderValue() + "&orderDirection=" + searchConditionDto.getOrderDirection() +
                "&pageNum=" + searchConditionDto.getPageNum();

        return queryString;
    }
}
