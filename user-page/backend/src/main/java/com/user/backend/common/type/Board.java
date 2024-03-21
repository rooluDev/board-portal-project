package com.user.backend.common.type;

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

    Board(String boardType){
        this.boardType = boardType;
    }


}
