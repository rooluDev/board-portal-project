package com.user.backend.common.utils;

/**
 * Custom Pagination Utils
 */
public class PaginationUtils {

    /**
     * 총 페이지 수 구하는 메소드
     * @param totalRowCount 게시물의 총 수
     * @param pageSize 한 페이지에 보여질 게시물의 수
     * @return 총 페이지의 수
     */
    public static int getTotalPageNum(int totalRowCount, int pageSize){
        return (int) Math.ceil((double) totalRowCount / pageSize);
    }
}
