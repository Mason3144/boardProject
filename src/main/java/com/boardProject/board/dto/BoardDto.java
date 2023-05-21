package com.boardProject.board.dto;

import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Content;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Post;
import com.boardProject.member.entity.Member;
import com.boardProject.validator.notSpace.NotSpace;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class BoardDto {
    @Getter
    @Builder
    @Setter
    public static class Post{
        private long postId;
        @NotBlank
        private long memberId;
        @NotBlank
        private String title;
        @NotBlank
        private com.boardProject.board.entity.Post.PostStatus postStatus;
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
        @NotSpace
        private com.boardProject.board.entity.Post.PostStatus postStatus;
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
        private com.boardProject.board.entity.Post.PostStatus postStatus;
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
