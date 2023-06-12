package com.boardProject.board.dto;

import lombok.Builder;
import lombok.Getter;

public class PhotoDto {
    @Getter
    @Builder
    public static class Response{
        private long photoId;
        private String filePath;
    }
}
