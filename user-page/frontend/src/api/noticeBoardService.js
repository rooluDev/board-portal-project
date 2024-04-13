import axios from "axios";

export const fetchNoticeBoardList = async (searchCondition) => {
    const queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}` +
        `&category=${searchCondition.category}&searchText=${searchCondition.searchText}&pageSize=${searchCondition.pageSize}` +
        `&orderValue=${searchCondition.orderValue}&orderDirection=${searchCondition.orderDirection}&pageNum=${searchCondition.pageNum}`;
    try {
        const res = await axios.get("/api/boards/notice" + queryString);
        return res.data;
    } catch (error) {
        throw new Error();
    }

}

export const fetchNoticeBoard = async (boardId) => {
    try {
        const res = await axios.get(`/api/board/notice/${boardId}`);
        return res.data;
    } catch (error) {
        throw new Error();
    }
}