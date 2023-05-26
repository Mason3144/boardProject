package com.boardProject.board.controller;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Content;
import com.boardProject.board.mapper.PostsMapper;
import com.boardProject.board.service.PostsService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/posts")
@Validated
public class PostsController {
    private final PostsService postsService;
    private final PostsMapper postsMapper;

    public PostsController(PostsService postsService, PostsMapper postsMapper) {
        this.postsService = postsService;
        this.postsMapper = postsMapper;
    }

    @PostMapping
    public ResponseEntity postPosts(@RequestBody @Valid PostsDto.Post requestBody){

        postsService.createPost(postsMapper.postDtoToPosts(requestBody));


        return null;
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity patchPost(){
        return null;
    }

    @GetMapping("/{post-id}")
    public ResponseEntity getPost(){
        postsService.getPost();

        return null;
    }

    @GetMapping
    public ResponseEntity getPosts(){
        return null;
    }

//    @GetMapping
//    public ResponseEntity searchPosts(){
//        return null;
//    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePost(){
        return null;
    }
}
