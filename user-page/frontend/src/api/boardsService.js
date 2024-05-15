import {api} from "@/api/apiConfig";

/**
 * GET /api/boards/all
 * 자유게시판, 문의게시판, 공지사항 최근 6개의 데이터, 갤러리게시판 최근 3개의 데이터 GET
 *
 * @returns {Promise<any>}
 * {
 *  freeBoardList : [],
 *  noticeBoardList : [],
 *  galleryBoardList : [],
 *  inquiryBoardList : []
 *  }
 */
export const fetchGetBoardListForMain = async () =>{
    const res = await api.get("/boards/all");
    return res.data;
}