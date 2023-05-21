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
        private long commentId;
        @NotBlank
        private long memberId;
        @NotBlank
        private long postId;
        @NotBlank
        private String comment;
    }
}
