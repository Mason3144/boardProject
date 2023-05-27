package com.boardProject.exception.businessLogicException;

import lombok.Getter;

public enum ExceptionCode {
    MEMBER_SLEEP(400, "Member sleep"),
    MEMBER_QUIT(400, "Member quit"),
    MEMBER_NOT_FOUND(404, "Member not found"),
    MEMBER_EXISTS(409, "Member exists"),
    MEMBER_NOT_AUTHORIZED(403,"Member not authorized"),
    POST_NOT_FOUND(404, "Post not found"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_MEMBER_STATUS(400, "Invalid member status");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
