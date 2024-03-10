package com.admin.backend.common.type;

/**
 * Board Type Enum
 */
public enum Board {
    FREE_BOARD("free"),
    NOTICE_BOARD("notice"),
    GALLERY_BOARD("gallery"),
    INQUIRY_BOARD("inquiry");

    private String boardType;

    Board(String boardType){
        this.boardType = boardType;
    }
}
