package com.user.backend.common.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


/**
 * Custom Error Code
 * {
 *     httpStatus: ,
 *     code: ,
 *     message: ,
 * }
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "게시물이 존재하지 않음"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "파일이 존재하지 않음"),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "A003", "회원이 존재하지 않음"),
    THUMBNAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A004", "썸네일 존재하지 않음"),
    NOT_LOGGED_IN(HttpStatus.FORBIDDEN, "A005", "로그인이 필요한 서비스"),
    NOT_MY_BOARD(HttpStatus.FORBIDDEN, "A006", "본인 게시물 아님"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "A007", "로그인 실패"),
    ILLEGAL_FILE_DATA(HttpStatus.UNPROCESSABLE_ENTITY, "A008", "파일의 데이터 에러"),
    ID_DUPLICATE(HttpStatus.CONFLICT, "A009", "아이디 중복"),
    DOWNLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "A010", "다운로드 실패"),
    STORAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "A011", "파일 저장 실패"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"A012","서버 에러"),
    ILLEGAL_BOARD_DATA(HttpStatus.UNPROCESSABLE_ENTITY,"A013","게시물 데이터 에러"),
    JOIN_FAIL(HttpStatus.UNPROCESSABLE_ENTITY,"A014","회원가입 실패");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
