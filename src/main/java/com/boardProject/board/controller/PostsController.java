package com.boardProject.board.controller;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Content;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.mapper.PostsMapper;
import com.boardProject.board.service.PostsService;
import com.boardProject.dto.SingleResponseDto;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.UriCreator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;


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
        Posts postCreated = postsService.createPost(postsMapper.postDtoToPosts(requestBody));

        URI location = UriCreator.createUri(UriCreator.DefaultUrl.POST_DEFAULT_URL.getUrl(),postCreated.getPostId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{post-id}")
    public ResponseEntity patchPost(){
        return null;
    }



    @GetMapping("/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive long postId){
        Posts post = postsService.getPost(postId);

        PostsDto.ResponseOnPost response = postsMapper.postsToResponseOnPost(post);

        System.out.println(response.getPostId());
        System.out.println();




//        return new ResponseEntity<>(new SingleResponseDto<>(, HttpStatus.OK));
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
