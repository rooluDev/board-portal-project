package com.user.backend.common.exception.handler;

import com.user.backend.common.exception.custom.*;
import com.user.backend.common.exception.response.ErrorCode;
import com.user.backend.common.exception.response.ErrorResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

/**
 * Controller Global Exception Handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * MemberIdExistedException Handler
     *
     * @param e MemberIdExistedException
     * @return ID_DUPLICATE(HttpStatus.CONFLICT, "A009", "아이디 중복")
     */
    @ExceptionHandler(MemberIdExistedException.class)
    public ResponseEntity handleMemberIdExistedException(MemberIdExistedException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * NotLoggedInException Handler
     *
     * @param e BoardNotFoundException
     * @return NOT_LOGGED_IN(HttpStatus.FORBIDDEN, "A005", "로그인이 필요한 서비스")
     */
    @ExceptionHandler(NotLoggedInException.class)
    public ResponseEntity handleNotLoggedInException(NotLoggedInException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * BoardNotFoundException Handler
     *
     * @param e BoardNotFoundException
     * @return BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "A001", "게시물이 존재하지 않음")
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity handleBoardNotFoundException(BoardNotFoundException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * MemberNotFoundException Hanlder
     *
     * @param e MemberNotFoundException
     * @return MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "A003", "회원이 존재하지 않음")
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity handleMemberNotFoundException(MemberNotFoundException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * FileNotFoundException Handler
     *
     * @param e FileNotFoundException
     * @return FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "A002", "파일이 존재하지 않음")
     */
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity handleFileNotFoundException(FileNotFoundException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * ThumbnailNotFoundException Handler
     *
     * @param e ThumbnailNotFoundException
     * @return THUMBNAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "A004", "썸네일 존재하지 않음")
     */
    @ExceptionHandler(ThumbnailNotFoundException.class)
    public ResponseEntity handleThumbnailNotFoundException(ThumbnailNotFoundException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * DownloadFailException Handler
     *
     * @param e DownloadFailException
     * @return DOWNLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "A010", "다운로드 실패")
     */
    @ExceptionHandler(DownloadFailException.class)
    public ResponseEntity handleDownloadFailException(DownloadFailException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * IllegalFileDataException
     *
     * @param e IllegalFileDataException
     * @return ILLEGAL_FILE_DATA(HttpStatus.UNPROCESSABLE_ENTITY, "A008", "파일의 데이터 에러")
     */
    @ExceptionHandler(IllegalFileDataException.class)
    public ResponseEntity handleIllegalFileDataException(IllegalFileDataException e) {
        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * StorageFailException
     *
     * @param e StorageFailException
     * @return STORAGE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "A011", "파일 저장 실패")
     */
    @ExceptionHandler(StorageFailException.class)
    public ResponseEntity handleStorageFailException(StorageFailException e) {

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }

    /**
     * MethodArgumentNotValidException
     *
     * @param methodArgumentNotValidException MethodArgumentNotValidException
     * @return HttpStatus.UNPROCESSABLE_ENTITY
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException methodArgumentNotValidException) {

        log.error(methodArgumentNotValidException.getBindingResult().toString());

        return ErrorResponseEntity.toResponseEntity(ErrorCode.ILLEGAL_FILE_DATA);
    }

    @ExceptionHandler(NotMyBoardException.class)
    public ResponseEntity handleNotMyBoardException(NotMyBoardException e){

        log.error(e.getErrorCode().getMessage());

        return ErrorResponseEntity.toResponseEntity(e.getErrorCode());
    }


//    @ExceptionHandler(SQLException.class)
//    public ResponseEntity handleSQLException() {
//
//        log.error("SQL Exception");
//
//        return ErrorResponseEntity.toResponseEntity(ErrorCode.SERVER_ERROR);
//    }
//
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity handleException(Exception exception) {
//
//        log.error("Exception");
//
//        return ErrorResponseEntity.toResponseEntity(ErrorCode.SERVER_ERROR);
//    }
}
