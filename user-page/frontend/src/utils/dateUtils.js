/**
 * newDateLimit보다 작으면 true 크면 false를 반환
 *
 * @param createdAt 생성일자
 * @param newDateLimit 한계점
 * @returns {boolean} boolean
 */
export const isNew = (createdAt, newDateLimit) => {
    const today = new Date();
    const difference = (today - new Date(createdAt)) / (1000 * 60 * 60 * 24);
    return difference <= newDateLimit
}