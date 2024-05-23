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

export const freeBoardValidator = (freeBoard, constraint) => {
    if (freeBoard.categoryId == -1) {
        throw new Error("카테고리를 선택하세요.");
    }
    textValidator(freeBoard.title, constraint.title.minLength, constraint.title.maxLength, "제목");
    textValidator(freeBoard.content, constraint.content.minLength, constraint.content.maxLength, "내용");
}


export const galleryBoardValidator = (galleryBoard, constraint) => {
    if (galleryBoard.categoryId == -1) {
        throw new Error("카테고리를 선택하세요.");
    }
    textValidator(galleryBoard.title, constraint.title.minLength, constraint.title.maxLength, "제목");
    textValidator(galleryBoard.content, constraint.content.minLength, constraint.content.maxLength, "내용");
}


export const inquiryBoardValidator = (inquiryBoard, constraint) => {
    textValidator(inquiryBoard.title, constraint.title.minLength, constraint.title.maxLength, "제목");
    textValidator(inquiryBoard.content, constraint.content.minLength, constraint.content.maxLength, "내용");
}

const passwordValidator = (password, passwordCheck, memberId, constraint) => {
    if (!password || password.length < constraint.minLength || password.length > constraint.maxLength) {
        throw new Error(`비밀번호는 ${constraint.minLength}자 이상 ${constraint.maxLength}자 이하이어야 합니다.`);
    }
    if (password !== passwordCheck) {
        throw new Error("비밀번호가 일치하지 않습니다.");
    }
    if (password === memberId) {
        throw new Error("비밀번호는 아이디와 동일할 수 없습니다.");
    }
    if (/(\w)\1{2,}/.test(password)) {
        throw new Error("비밀번호는 동일한 문자를 3번 이상 연속할 수 없습니다.");
    }
    if (constraint.regex && !new RegExp(constraint.regex).test(password)) {
        throw new Error("비밀번호가 유효하지 않습니다.");
    }
};

export const joinValidator = (joinForm, constraint) => {
    textValidator(joinForm.memberId, constraint.memberId.minLength, constraint.memberId.maxLength,"아이디");
    textValidator(joinForm.memberName, constraint.memberName.minLength, constraint.memberName.maxLength,"이름");
    passwordValidator(joinForm.password, joinForm.passwordCheck, joinForm.memberId, constraint.password);
}