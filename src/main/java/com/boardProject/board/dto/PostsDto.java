package com.boardProject.board.dto;

import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Posts;
import com.boardProject.member.dto.MemberDto;
import com.boardProject.validator.notSpace.NotSpace;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class PostsDto {
    @Getter
    @Builder
    public static class Post{
        @NotBlank
        private String title;
        @NotNull
        private Posts.PostStatus postStatus;
        @NotBlank
        private String content;

    }
    @Getter
    @Builder
    @Setter
    public static class Patch{
        private long postId;
        @NotSpace
        private String title;
        @NotNull
        private Posts.PostStatus postStatus;
        @NotSpace
        private String content;
    }
    @Getter
    @Builder
    public static class ResponseOnBoard{
        private long postId;
        private String title;
        private int views;
        private MemberDto.Response writer;
        private Posts.PostStatus postStatus;
        private LocalDateTime createdAt;
        private int commentsNumber;
        private boolean isMine;
    }

    @Getter
    @Builder
    public static class ResponseOnPost{
        private long postId;
        private String title;
        private int views;
        private MemberDto.Response writer;
        private String content;
        private Posts.PostStatus postStatus;
        private LocalDateTime createdAt;
        private LikeDto.Response likes;
        private List<Comment> comments;
        private boolean isMine;
    }
}
