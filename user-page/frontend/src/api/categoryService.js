import axios from "axios";

export const fetchCategoryListByBoardType = async (boardType) => {
    try {
        const res = await axios.get(`/api/categories?boardType=${boardType}`);
        return res.data;
    } catch (error){
        throw new Error();
    }
}