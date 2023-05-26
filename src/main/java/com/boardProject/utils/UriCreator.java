package com.boardProject.utils;

import lombok.Getter;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriCreator {
    public static URI createUri(String defaultUrl, long resourceId) {
        return UriComponentsBuilder
                .newInstance()
                .path(defaultUrl + "/{resource-id}")
                .buildAndExpand(resourceId)
                .toUri();
    }
    public enum DefaultUrl{
        MEMBER_DEFAULT_URL("/v1/members"),
        POST_DEFAULT_URL("/v1/posts");

        @Getter
        private String url;

        DefaultUrl(String url) {
            this.url = url;
        }
    }
}