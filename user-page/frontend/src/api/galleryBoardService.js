import axios from "axios";

export const fetchGalleryBoardList = async (searchCondition) => {
    const queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}&category=${searchCondition.category}` +
        `&searchText=${searchCondition.searchText}&pageSize=${searchCondition.pageSize}&orderValue=${searchCondition.orderValue}` +
        `&orderDirection=${searchCondition.orderDirection}&pageNum=${searchCondition.pageNum}`;
    try {
        const res = await axios.get("/api/boards/gallery" + queryString);
        return res.data;
    } catch (error) {
        throw new Error();
    }
}

export const fetchGalleryBoard = async (boardId) => {
    try {
        const res = await axios.get(`/api/board/gallery/${boardId}`);
        return res.data;
    } catch (error) {
        throw new Error();
    }
}

export const fetchAddGalleryBoard = async (formData, accessToken) => {
    try {
        await axios.post('/api/board/gallery', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${accessToken}`
            }
        })
    } catch (error) {
        throw new Error();
    }
}