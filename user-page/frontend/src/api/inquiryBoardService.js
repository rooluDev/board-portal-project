import {Board} from "@/type/boardType";
import {parseToQueryString} from "@/utils/stringUtils";
import {api} from "@/api/apiConfig";

/**
 * GET /api/boards/inquiry
 * 검색조건을 통한 문의게시판 데이터 가져오기
 *
 * @param searchCondition 검색조건
 * @returns {Promise<any>}
 * {
 *     totalPageNum: 0,
 *     inquiryBoardList: [],
 *     searchCondition:{}
 * }
 */
export const fetchGetInquiryBoardList = async (searchCondition) => {
    const queryString = parseToQueryString(searchCondition, Board.INQUIRY_BOARD);
    const res = await api.get("/boards/inquiry" + queryString);
    return res.data;
}
/**
 * GET /api/board/inquiry/boardId
 * 문의 게시판 데이터 가져오기
 *
 * @param boardId PathVariable
 * @returns {Promise<any>} inquiryBoard
 */
export const fetchGetInquiryBoard = async (boardId) => {
    const res = await api.get(`/board/inquiry/${boardId}`);
    return res.data;
}

/**
 * POST /api/board/inquiry
 * 문의게시판 추가
 *
 * @param board (title, content, isSecret)
 */
export const fetchAddInquiryBoard = async (board) => {
    await api.post('/board/inquiry', board)
}

/**
 * PATCH /api/board/inquiry/boardId/increase-view
 * 조회수 1 증가
 *
 * @param boardId PathVariable
 */
export const fetchAddInquiryView = async (boardId) => {
    await api.patch(`/board/inquiry/${boardId}/increase-view`);
}

/**
 * GET /api/board/inquiry/boardId/check-author
 * 문의 게시판의 작성자 확인
 *
 * @param accessToken JWT
 */
export const fetchCheckInquiryAuthor = async (boardId) => {
    await api.get(`/board/inquiry/${boardId}/check-author`);
}

/**
 * DELETE /api/board/inquiry/boardId
 * 게시물 삭제
 *
 * @param boardId PathVariable
 */
export const fetchDeleteInquiryBoard = async (boardId) => {
    await api.delete(`/board/inquiry/${boardId}`);
}

/**
 * PUT /api/board/inquiry/boardId
 * 문의 게시판 수정
 *
 * @param board inquiryBoard Form Data
 */
export const fetchModifyInquiryBoard = async (board) => {
    await api.put(`/board/inquiry/${board.boardId}`,
        {
            title: board.title,
            content: board.content,
            isSecret: board.isSecret
        })
}