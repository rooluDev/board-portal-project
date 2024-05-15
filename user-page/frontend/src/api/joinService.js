import {api} from "@/api/apiConfig";

/**
 * POST /api/member
 * 회원가입
 *
 * @param joinForm
 * {
 *   memberId:'',
 *   password:'',
 *   passwordCheck:'',
 *   memberName:''
 * }
 */
export const fetchAddMember = async (joinForm) => {
    await api.post("/member", joinForm);
}

/**
 * GET /api/member/check-duplicate?memberId=
 * memberId 중복검사
 *
 * @param memberId memberId
 */
// TODO : 헤더?
export const fetchCheckDuplicateMemberId = async (memberId) => {
    await api.get(`/member/check-duplicate?memberId=${memberId}`)
}