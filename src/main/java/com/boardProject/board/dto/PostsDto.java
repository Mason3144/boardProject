package com.boardProject.board.dto;

import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Content;
import com.boardProject.board.entity.Posts;
import com.boardProject.validator.notSpace.NotSpace;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class PostsDto {
    @Getter
    @Builder
    @Setter
    public static class Post{
        @NotBlank
        private String title;
        @NotNull
        private Posts.PostStatus postStatus;
        @NotBlank
        private String text;

    }
    @Getter
    @Builder
    @Setter
    public static class Patch{
        private long postId;
        @NotSpace
        private String title;
        @NotSpace
        private Posts.PostStatus postStatus;
        @NotSpace
        private String content;
    }
    @Getter
    @Builder
    @Setter
    public static class ResponseOnBoard{
        private long postId;
        private String title;
        private int views;
        private String writer;
        private Posts.PostStatus postStatus;
        private LocalDateTime createdAt;
        private int commentsNumber;
    }

    @Getter
    @Builder
    @Setter
    public static class ResponseOnPost{
        private long postId;
        private String title;
        private int views;
        private String writer;
        private LocalDateTime createdAt;
        private LikeDto.Response likes;
        private List<Comment> comments;
    }
}
