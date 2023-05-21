package com.boardProject.auth.dto;

import lombok.Builder;
import lombok.Getter;

public class LoginDto {
    @Getter
    public static class Request{
        private String username;
        private String password;
    }
    @Getter
    @Builder
    public static class Response{
        private String email;
        private String name;
    }
}
