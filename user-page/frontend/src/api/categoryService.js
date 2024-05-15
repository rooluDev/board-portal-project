import {api} from "@/api/apiConfig";

/**
 * GET /api/categories?boardType=
 * 보드타입에 맞는 카테고리 리스트 가져오기
 *
 * @param boardType boardType
 * @returns {Promise<any>} categoryList
 */
export const fetchCategoryListByBoardType = async (boardType) => {
    const res = await api.get(`/categories?boardType=${boardType}`);
    return res.data;
}