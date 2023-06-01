package com.boardProject.board.controller;

import com.boardProject.board.dto.CommentDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.mapper.CommentsMapper;
import com.boardProject.board.service.CommentService;
import com.boardProject.board.service.LikeService;
import com.boardProject.dto.SingleResponseDto;
import com.boardProject.utils.UriCreator;
import org.springframework.http.HttpStatus;
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
    private final CommentsMapper commentsMapper;

    public CommentController(CommentService commentService, CommentsMapper commentsMapper) {
        this.commentService = commentService;
        this.commentsMapper = commentsMapper;
    }

    @PostMapping()
    public ResponseEntity postComment(@RequestParam("postid") @Positive long postId,
                                        @RequestBody @Valid CommentDto.Post requestBody){
        requestBody.setPostId(postId);
        Comment comment = commentService.createComment(commentsMapper.commentPostDtoToComment(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(commentsMapper.commentToCommentResponseDto(comment)), HttpStatus.OK
        );
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity deleteComment(@PathVariable("comment-id") @Positive long commentId){
        commentService.removeComment(commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PatchMapping("/{comment-id}")
    public ResponseEntity patchComment(@PathVariable("comment-id") @Positive long commentId,
                                        @RequestBody @Valid CommentDto.Patch requestBody){
        requestBody.setCommentId(commentId);

        Comment comment = commentService.updateComment(commentsMapper.commentPatchDtoToComment(requestBody));

        return new ResponseEntity<>(
                new SingleResponseDto<>(commentsMapper.commentToCommentResponseDto(comment)), HttpStatus.OK
        );
    }
}
