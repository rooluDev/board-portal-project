import axios from "axios";

export const loginService = async (memberId, password) => {
    try {
        const res = await axios.post('/api/login', {memberId, password});
        return res.data;
    } catch (error) {
        return null;
    }
}
