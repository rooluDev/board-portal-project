package com.admin.backend.common.type;

import lombok.Getter;

/**
 * Board Type Enum
 */
@Getter
public enum Board {
    FREE_BOARD("free"),
    NOTICE_BOARD("notice"),
    GALLERY_BOARD("gallery"),
    INQUIRY_BOARD("inquiry");

    private String boardType;

    /**
     * Board 열거형 생성자
     *
     * @param boardType DB 및 경로에 사용되는 게시판 타입 문자열
     */
    Board(String boardType){
        this.boardType = boardType;
    }


}
