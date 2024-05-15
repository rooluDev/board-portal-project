import {api} from "@/api/apiConfig";

/**
 * GET /api/member
 * jwt와 일치하는 member 가져오기
 *
 * @returns {Promise<any>} member
 */
export const fetchGetMember = async () => {
    const res = await api.get(`/member`)
    return res.data;
}