package com.boardProject.board.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class CommentDto {
    @Getter
    @Builder
    @Setter
    public static class Post{
        private long postId;
        @NotBlank
        private String content;
    }

    @Getter
    @Builder
    @Setter
    public static class Patch{
        private long commentId;
        @NotBlank
        private String content;
    }

    @Getter
    @Builder
    @Setter
    public static class Response{
        private long commentId;
        private long memberId;
        private long postId;
        private String comment;
    }
}
