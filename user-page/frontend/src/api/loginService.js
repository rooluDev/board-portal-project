import {api} from "@/api/apiConfig";

/**
 * POST /api/login
 * 로그인
 *
 * @param loginForm
 * {
 *     memberId,
 *     password
 * }
 */
export const fetchLogin = async (loginForm) => {
    const res = await api.post('/login', loginForm);
    return res.data;
}