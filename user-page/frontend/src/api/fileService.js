import {api} from "@/api/apiConfig";

/**
 * GET /api/file/fileId/download
 * 파일 다운로드 리소스 가져오기
 *
 * @param fileId PathVariable
 * @returns {Promise<any>} blob
 */
export async function downloadFile(fileId) {
    const res = await api.get(`/file/${fileId}/download`, {
        responseType: 'blob',
    });
    return res.data;
}