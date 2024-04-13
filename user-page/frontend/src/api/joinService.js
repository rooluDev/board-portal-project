import axios from "axios";

export const joinService = async (memberId, password, passwordCheck, memberName) => {
    try {
        await axios.post("/api/member", {
            memberId: memberId,
            password: password,
            passwordCheck: passwordCheck,
            memberName: memberName,
        });
        return "success";
    } catch (error) {
        throw new Error();
    }
}

export const checkDuplicateMemberId = async (memberId) => {
    try {
        await axios.get(`/api/member/check-duplicate?memberId=${memberId}`)
        return "success";
    } catch (error) {
        return "error";
    }
}