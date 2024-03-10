package com.admin.backend.common.utils;

/**
 * 페이지네이션 유틸 클래스
 */
public class PaginationUtils {

    /**
     * 총 페이지 수 구하는 메소드
     * @param totalRowCount
     * @param pageSize
     * @return
     */
    public static int getTotalPageNum(int totalRowCount, int pageSize){
        return (int) Math.ceil((double) totalRowCount / pageSize);
    }
}
