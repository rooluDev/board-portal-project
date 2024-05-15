import moment from "moment";

/**
 * sql Timestamp 형식을 format 형식의 Stirng으로 변환
 *
 * @param timestamp Timestamp형 String
 * @param format Format
 * @returns {string} Format형태의 String
 */
export const parseStringByFormat = (timestamp, format) => {
    return moment(timestamp).format(format);
}