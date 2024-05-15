package com.admin.backend.common.utils;

import com.admin.backend.dto.FileDto;
import com.admin.backend.dto.SearchConditionDto;
import com.admin.backend.dto.ThumbnailDto;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Custom String Utils
 */
public class StringUtils {

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

    /**
     * FileDto를 매개변수로 받아 상대경로로 반환
     * @param fileDto FileDto
     * @return 상대경로 ex : /gallery/physicalName.png
     */
    public static String parseToPath(FileDto fileDto) {
        return fileDto.getFilePath() + "/" + fileDto.getPhysicalName() + "." + fileDto.getExtension();
    }

    /**
     * ThumbnailDto를 매개변수로 받아 상대경로로 반환
     *
     * @param thumbnailDto ThumbnailDto
     * @return 상대경로 ex : /thumbnail/physicalName.png
     */
    public static String parseToPath(ThumbnailDto thumbnailDto) {
        return thumbnailDto.getFilePath() + "/" + thumbnailDto.getPhysicalName() + "." + thumbnailDto.getExtension();
    }
}
