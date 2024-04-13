import axios from "axios";

export const fetchComment = async (content, boardId, boardType, accessToken) => {
    try {
        const res = await axios.post("/api/comment",
            {
                content,
                boardId,
                boardType
            },
            {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            })
        return res.data;
    } catch (error) {
        throw new Error();
    }
}