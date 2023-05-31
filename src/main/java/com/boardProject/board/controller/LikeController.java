package com.boardProject.board.controller;

import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.mapper.LikeMapper;
import com.boardProject.board.service.LikeService;
import com.boardProject.dto.SingleResponseDto;
import com.boardProject.utils.UriCreator;
import org.springframework.http.HttpStatus;
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
    private final LikeMapper likeMapper;

    public LikeController(LikeService likeService, LikeMapper likeMapper) {
        this.likeService = likeService;
        this.likeMapper = likeMapper;
    }

    @PostMapping("/toggle")
    public ResponseEntity postLike(@RequestParam("postid") @Positive long postId){
        Likes like = likeService.toggleLike(postId);

        LikeDto.ResponseOnToggle response = likeMapper.likesToLikeDto(like);

        return new ResponseEntity<>(new SingleResponseDto<>(response), HttpStatus.OK);
    }
}
