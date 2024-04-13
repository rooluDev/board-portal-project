import axios from "axios";

export const boardListForMain = async () => {
    try{
        const res = await axios.get("/api/boards/all");
        return res.data;
    }catch (error){
        throw new Error();
    }
}

export const fetchFreeBoardList = async (searchCondition) => {
    const queryString = `?startDate=${searchCondition.startDate}&endDate=${searchCondition.endDate}&category=${searchCondition.category}` +
        `&searchText=${searchCondition.searchText}&pageSize=${searchCondition.pageSize}&orderValue=${searchCondition.orderValue}` +
        `&orderDirection=${searchCondition.orderDirection}&pageNum=${searchCondition.pageNum}`;
    try {
        const res = await axios.get("/api/boards/free" + queryString);
        return res.data;
    } catch (error){
        throw new Error();
    }
}
export const fetchFreeBoard = async (boardId) => {
    try{
        const res = await axios.get(`/api/board/free/${boardId}`);
        return res.data;
    }catch (error){
        throw new Error();
    }
}

export const fetchDeleteBoard = async (boardId) => {
    try {
        await axios.put(`/api/board/free/${boardId}`);
    } catch (error){
        throw new Error();
    }
}

export const fetchAddFreeBoard = async (formData,accessToken) => {
    try{
        await axios.post(`/api/board/free`,formData,{
            headers:{
                'Content-Type': 'multipart/form-data',
                Authorization: `Bearer ${accessToken}`
            }
        })
    } catch (error) {
        throw new Error();
    }
}