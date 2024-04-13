import moment from "moment";

export const parseStringByFormat = (timestamp, format) => {
    return moment(timestamp).format(format);
}