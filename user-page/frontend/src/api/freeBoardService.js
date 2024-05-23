import {parseToQueryString} from "@/utils/stringUtils";
import {Board} from "@/type/boardType";
import {api} from "@/api/apiConfig";

/**
 * GET /api/boards/free
 * 검색조건으로 자유게시판 리스트 가져오기
 *
 * @param searchCondition 검색조건
 * @returns {Promise<any>}
 * {
 *     freeBoardList:[],
 *     categoryList:[],
 *     searchCondition:{},
 *     totalPageNum: 0
 * }
 */
export const fetchGetFreeBoardList = async (searchCondition) => {
    const queryString = parseToQueryString(searchCondition, Board.FREE_BOARD);
    const res = await api.get("/boards/free" + queryString);
    return res.data;
}
/**
 * GET /api/board/free/boardId
 * 자유게시물 데이터 가져오기
 *
 * @param boardId PathVariable
 * @returns {Promise<any>} freeBoard
 */
export const fetchGetFreeBoard = async (boardId) => {
    const res = await api.get(`/board/free/${boardId}`);
    return res.data;
}

/**
 * DELETE /api/board/free/boardId
 * 자유게시판 삭제
 *
 * @param boardId PathVariable
 */
export const fetchDeleteFreeBoard = async (boardId) => {
    await api.delete(`/board/free/${boardId}`);
}

/**
 * POST /api/board/free
 * 자유게시판 추가
 *
 * @param formData 폼데이터
 */
export const fetchAddFreeBoard = async (formData) => {
    await api.post(`/board/free`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    })
}

/**
 * PATCH /api/board/free/boardId/increase-view
 * 조회수 1 증가
 *
 * @param boardId PathVariable
 */
export const fetchAddFreeView = async (boardId) => {
    await api.patch(`/board/free/${boardId}/increase-view`);
}

/**
 * GET /api/board/free/boardId/check-author
 * 게시물 작성자 확인
 *
 * @param boardId PathVariable
 */
export const fetchCheckFreeAuthor = async (boardId) => {
    const res = await api.get(`/board/free/${boardId}/check-author`);
    return res.data;
}

/**
 * PUT /api/board/free/boardId
 * 게시물 수정
 *
 * @param boardId PathVariable
 * @param formData FreeBoard Form Data
 */
export const fetchModifyFreeBoard = async (boardId, formData) => {
    await api.put(`/board/free/${boardId}`, formData,{
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}