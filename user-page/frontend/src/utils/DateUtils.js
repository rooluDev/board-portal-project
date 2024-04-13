export const isNew = (createdAt, newDateLimit) => {
    const today = new Date();
    const difference = (today - new Date(createdAt)) / (1000 * 60 * 60 * 24);
    return difference <= newDateLimit
}