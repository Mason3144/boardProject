package com.boardProject.member.dto;


import com.boardProject.member.entity.Member;
import com.boardProject.validator.notSpace.NotSpace;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MemberDto {
    @Getter
    @Builder
    @Setter
    public static class Post{
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String name;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        private String password;

    }
    @Getter
    @Builder
    @Setter
    public static class Patch{
        private long memberId;
        @Email
        @NotSpace
        private String email;
        @NotSpace
        private String name;
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$")
        @NotSpace
        private String password;
    }
    @Getter
    @Builder
    @Setter
    public static class Response{
        private long memberId;
        private String email;
        private String name;
        private Member.MemberStatus memberStatus;
    }
}
