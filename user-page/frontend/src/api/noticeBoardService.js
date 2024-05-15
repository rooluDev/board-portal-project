import {parseToQueryString} from "@/utils/stringUtils";
import {Board} from "@/type/boardType";
import {api} from "@/api/apiConfig";

/**
 * GET /api/boards/notice
 * 검색조건을 통한 공지사항 데이터 GET
 *
 * @param searchCondition 검색조건
 * @returns {Promise<any>}
 * {
 *  totalPageNum : 0,
 *  noticeBoardList : [],
 *  fixedNoticeBoardList : [],
 *  categoryList : [],
 *  searchCondition : {}
 * }
 */
export const fetchGetNoticeBoardList = async (searchCondition) => {
    const queryString = parseToQueryString(searchCondition, Board.NOTICE_BOARD);
    const res = await api.get("/boards/notice" + queryString);
    return res.data;
}

/**
 * GET /api/board/notice/boardId
 * 공지사항 데이터 GET
 *
 * @param boardId PathVariable
 * @returns {Promise<any>} noticeBoard
 */
export const fetchGetNoticeBoard = async (boardId) => {
    const res = await api.get(`/board/notice/${boardId}`);
    return res.data;
}

/**
 * PATCH /api/board/notice/${boardId}/increase-view
 * 공지사항 조회수 1 증가
 *
 * @param boardId PathVariable
 */
export const fetchAddNoticeView = async (boardId) => {
    await api.patch(`/board/notice/${boardId}/increase-view`);
}