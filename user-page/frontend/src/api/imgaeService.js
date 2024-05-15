import {api} from "@/api/apiConfig";

/**
 * GET /api/thumbnail/thumbnailId
 * thumbnail 이미지 리소스 GET
 *
 * @param thumbnailId ( tb_thumbnail pk )
 * @returns {Promise<any>} blob 형태의 썸네일 리소스
 */
export const fetchGetThumbnailResource = async (thumbnailId) => {
    const res = await api.get(`/thumbnail/${thumbnailId}`, {
        responseType: 'blob'
    })
    return res.data;
}

/**
 * GET /api/file/fileId
 * fileId 이미지 리소스 GET
 *
 * @param thumbnailId ( tb_file pk )
 * @returns {Promise<any>} blob 형태의 썸네일 리소스
 */
export const fetchGetFileResource = async (fileId) => {
    const res = await api.get(`/file/${fileId}`, {
        responseType: 'blob'
    })
    return res.data;

}