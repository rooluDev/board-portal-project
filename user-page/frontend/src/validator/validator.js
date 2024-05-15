const textValidator = (text, minLength, maxLength, fieldName) => {
    if (text === '') {
        throw new Error(`${fieldName}을 입력하세요.`);
    } else if (text.length < minLength) {
        throw new Error(`${fieldName}을 ${minLength}자 이상으로 입력하세요.`);
    } else if (text.length > maxLength) {
        throw new Error(`${fieldName}을 ${maxLength}자 미만으로 입력하세요.`);
    }
}

export const isValidFileSize = (fileSize, maxSize) => {
    return fileSize < maxSize;
}

export const validateFileLength = (fileList, minLength, maxLength) => {
    if (fileList == undefined || fileList.length < minLength) {
        throw new Error(`파일을 ${minLength}개 이상 첨부하세요.`);
    } else if (fileList.length > maxLength) {
        throw new Error(`파일을 ${maxLength}개 이상 첨부할 수 없습니다.`);
    }
}

export const replaceEmptyFileList = (fileList) => {
    return fileList.filter(file => file != null);
}

// export const validateFileLengthForModify = (fileList, deletedFileList,minLength, maxLength) => {
//     console.log(fileList.length);
//     console.log(deletedFileList.length);
// }

export const freeBoardValidator = (freeBoard, constraint) => {
    if (freeBoard.categoryId == -1) {
        throw new Error("카테고리를 선택하세요.");
    }
    textValidator(freeBoard.title, constraint.title.minLength, constraint.title.minLength, "제목");
    textValidator(freeBoard.content, constraint.content.minLength, constraint.content.maxLength, "내용");
}


export const galleryBoardValidator = (galleryBoard, constraint) => {
    textValidator(galleryBoard.title, 1, 100, "제목");
    textValidator(galleryBoard.content, 1, 4000, "내용");
    if (galleryBoard.categoryId == -1) {
        throw new Error("카테고리를 선택하세요.");
    }
}


export const inquiryBoardValidator = (inquiryBoard) => {
    textValidator(inquiryBoard.title, 1, 100, "제목");
    textValidator(inquiryBoard.content, 1, 4000, "내용");
}