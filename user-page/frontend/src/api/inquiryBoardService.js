import axios from "axios";

export const fetchInquiryBoardList = async (searchCondition) => {
    const queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}` +
        `&searchText=${searchCondition.searchText}&pageSize=${searchCondition.pageSize}&orderValue=${searchCondition.orderValue}` +
        `&orderDirection=${searchCondition.orderDirection}&pageNum=${searchCondition.pageNum}`;
    try {
        const res = await axios.get("/api/boards/inquiry" + queryString,{});
        return res.data;
    } catch (error) {
        throw new Error();
    }
}

export const fetchInquiryBoard = async (boardId, accessToken) => {
    try {
        const res = await axios.get(`/api/board/inquiry/${boardId}`, {
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        });
        return res.data;
    } catch (error) {
        throw new Error();
    }
}

export const fetchAddInquiryBoard = async (board,accessToken) => {
    try {
        await axios.post('/api/board/inquiry', board,{
            headers: {
                Authorization: `Bearer ${accessToken}`
            }
        })
    } catch (error) {
        throw new Error()
    }
}