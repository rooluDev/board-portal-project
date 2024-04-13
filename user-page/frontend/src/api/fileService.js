import axios from "axios";

export async function downloadFile(fileId) {
    const res = await axios.get(`/api/file/${fileId}/download`, {
        responseType: 'blob',
    });
    return res.data;
}