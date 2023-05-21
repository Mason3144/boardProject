package com.boardProject.board.dto;

import lombok.Builder;
import lombok.Getter;

public class LikeDto {
    @Getter
    @Builder
    public static class Response{
        private int totalLikes;
        private boolean isLiked;
    }
}
