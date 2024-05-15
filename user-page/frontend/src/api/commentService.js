import {api} from "@/api/apiConfig";

/**
 * POST /api/comment
 * 댓글 등록
 *
 * @param content 내용
 * @param boardId boardId ( pk )
 * @param boardType boardType
 * @returns {Promise<any>} boardId, boardType에 등록된 댓글 리스트
 */
export const fetchAddComment = async (content, boardId, boardType) => {
    const res = await api.post("/comment",
        {
            content,
            boardId,
            boardType
        },
    )
    return res.data;
}

/**
 * DELETE /api/comment/commentId
 * 댓글 삭제
 *
 * @param commentId PathVariable
 */
export const fetchDeleteComment = async (commentId) => {
    await api.delete(`/comment/${commentId}`)
}