import {Board} from "@/type/boardType";

/**
 * Text가 일정 길이가 넘어가면 그때부터 ...추가
 *
 * @param text Text
 * @param maxLength 최대길이
 * @returns {*|string} ... 추가한 제목
 */
export const truncateText = (text, maxLength) => {
    if (text.length <= maxLength) {
        return text;
    } else {
        return text.substring(0, maxLength) + '...';
    }
}
/**
 * 검색조건 Object를 보드 타입의 맞게 쿼리스트링으로 변환
 *
 * @param searchCondition 검색조건
 * @param boardType 보드 종류
 * @returns {*|string} 쿼리스트링
 */
export const parseToQueryString = (searchCondition, boardType) => {
    let queryString = '';
    switch (boardType) {
        case Board.NOTICE_BOARD:
        case Board.FREE_BOARD:
        case Board.GALLERY_BOARD:
            queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}` +
                `&category=${searchCondition.category}&searchText=${searchCondition.searchText}&pageSize=${searchCondition.pageSize}` +
                `&orderValue=${searchCondition.orderValue}&orderDirection=${searchCondition.orderDirection}&pageNum=${searchCondition.pageNum}`;
            return queryString;

        case Board.INQUIRY_BOARD:
            queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}` +
                `&searchText=${searchCondition.searchText}&pageSize=${searchCondition.pageSize}&orderValue=${searchCondition.orderValue}` +
                `&orderDirection=${searchCondition.orderDirection}&pageNum=${searchCondition.pageNum}&my=${searchCondition.my}`;
            return queryString;
    }

}