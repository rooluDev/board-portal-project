export const truncateTitle = (title, maxLength) => {
    if (title.length <= maxLength) {
        return title;
    } else {
        return title.substring(0, maxLength) + '...';
    }
}