import {Board} from "@/type/boardType";
import {parseToQueryString} from "@/utils/stringUtils";
import {api} from "@/api/apiConfig";

/**
 * GET /api/boards/gallery
 * 검색조건으로 갤러리 리스트 가져오기
 *
 * @param searchCondition 검색조건
 * @returns {Promise<any>}
 * {
 *     galleryBoardList:[],
 *     categoryList:[],
 *     searchCondition:{},
 *     totalPageNum: 0
 * }
 */
export const fetchGetGalleryBoardList = async (searchCondition) => {
    const queryString = parseToQueryString(searchCondition, Board.GALLERY_BOARD);
    const res = await api.get("/boards/gallery" + queryString);
    return res.data;
}

/**
 * GET /api/board/gallery/boardId
 * 특정 갤러리 게시물 데이터 가져오기
 *
 * @param boardId PathVariable
 * @returns {Promise<any>} galleryBoard
 */
export const fetchGetGalleryBoard = async (boardId) => {
    const res = await api.get(`/board/gallery/${boardId}`);
    return res.data;
}

/**
 * POST /api/board/gallery
 * 갤러리게시판 추가
 *
 * @param formData 폼데이터
 */
export const fetchAddGalleryBoard = async (formData) => {
    await api.post('/board/gallery', formData, {
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
export const fetchAddGalleryView = async (boardId) => {
    await api.patch(`/board/gallery/${boardId}/increase-view`);
}

/**
 * GET /api/board/gallery/boardId/check-author
 * 게시물 작성자 확인
 *
 * @param boardId PathVariable
 */
export const fetchCheckGalleryAuthor = async (boardId) => {
    await api.get(`/board/gallery/${boardId}/check-author`);
}

/**
 * DELETE /api/board/gallery/boardId
 * 게시물 삭제
 *
 * @param boardId PathVariable
 */
export const fetchDeleteGalleryBoard = async (boardId) => {
    await api.delete(`/board/gallery/${boardId}`)
}

/**
 * PUT /api/board/gallery/boardId
 * 게시물 수정
 *
 * @param boardId PathVariable
 * @param formData 폼데이터
 */
export const fetchModifyGalleryBoard = async (boardId, formData) => {
    await api.put(`/board/gallery/${boardId}`, formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}