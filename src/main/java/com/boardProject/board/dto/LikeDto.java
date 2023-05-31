package com.boardProject.board.dto;

import lombok.Builder;
import lombok.Getter;

public class LikeDto {
    @Getter
    @Builder
    public static class ResponseOnPost{
        private int totalLikes;
        private boolean isLiked;
    }

    @Getter
    @Builder
    public static class ResponseOnToggle{
        private long memberId;
        private long postId;
        private boolean isLiked;
    }
}
