package com.boardProject.board.controller;

import com.boardProject.board.service.LikeService;
import com.boardProject.utils.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/v1/like")
@Validated
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping("/toggle")
    public ResponseEntity postLike(@RequestParam("postid") long postId){
        likeService.toggleLike(postId);

        URI location = UriCreator.createUri(UriCreator.DefaultUrl.POST_DEFAULT_URL.getUrl(),postId);

        return ResponseEntity.created(location).build();
    }
}
