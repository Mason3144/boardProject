package com.boardProject.board.controller;

import com.boardProject.board.dto.CommentDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.service.CommentService;
import com.boardProject.board.service.LikeService;
import com.boardProject.utils.UriCreator;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;

@RestController
@RequestMapping("/v1/comment")
@Validated
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity postComment(@RequestParam("postid") @Positive long postId,
                                        @RequestBody @Valid CommentDto.Post requestBody){


        return null;
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") @Positive long commentId){


        return null;
    }



    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@PathVariable("comment-id") @Positive long commentId,
                                        @RequestBody @Valid CommentDto.Patch requestBody){

        return null;
    }
}
