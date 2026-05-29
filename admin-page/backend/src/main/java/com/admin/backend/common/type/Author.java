package com.admin.backend.common.type;

import lombok.Getter;

/**
 * Author Type Enum
 */
@Getter
public enum Author {
    ADMIN("admin"),
    MEMBER("member");

    private String authorType;

    /**
     * Author 열거형 생성자
     *
     * @param authorType DB에 저장되는 작성자 타입 문자열
     */
    Author(String authorType){
        this.authorType = authorType;
    }
}
