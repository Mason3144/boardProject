package com.boardProject.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class PageDto {
    @Getter
    @Builder
    public static class Request{
        @Positive
        private int page;
        @Positive
        private int size;
    }
    @Getter
    @Builder
    public static class Response{
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
    }

}
