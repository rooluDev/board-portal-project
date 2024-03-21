package com.user.backend.common.type;

import lombok.Getter;

/**
 * Author Type Enum
 */
@Getter
public enum Author {
    ADMIN("admin"),
    MEMBER("member");

    private String authorType;

    Author(String authorType){
        this.authorType = authorType;
    }
}
