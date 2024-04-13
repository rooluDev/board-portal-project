import axios from "axios";

export const fetchMemberName = async (accessToken) => {
    try{
        const res = await axios.get(`/api/member-name`,{
            headers:{
                Authorization: `Bearer ${accessToken}`
            }
        })
        return res.data;
    }catch (error){
        throw new Error();
    }
}