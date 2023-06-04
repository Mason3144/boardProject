package com.boardProject.board.controller;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.mapper.PostsMapper;
import com.boardProject.board.service.PostsService;
import com.boardProject.dto.MultiResponseDto;
import com.boardProject.dto.SingleResponseDto;
import com.boardProject.utils.UriCreator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;


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

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity postPosts(@RequestPart @Valid PostsDto.Post requestBody,
                                    @RequestPart(required = false) List<MultipartFile> photoImgs){
        Posts createdPost = postsService.createPost(postsMapper.postDtoToPosts(requestBody), photoImgs);

        URI location = UriCreator.createUri(UriCreator.DefaultUrl.POST_DEFAULT_URL.getUrl(),createdPost.getPostId());

        return ResponseEntity.created(location).build();
    }


    @PatchMapping("/{post-id}")
    public ResponseEntity patchPost(@PathVariable("post-id") @Positive long postId,
                                    @RequestBody @Valid PostsDto.Patch requestBody){
        requestBody.setPostId(postId);

        Posts updatedPost = postsService.updatePost(postsMapper.patchDtoToPosts(requestBody));

        URI location = UriCreator.createUri(UriCreator.DefaultUrl.POST_DEFAULT_URL.getUrl(),updatedPost.getPostId());

        return ResponseEntity.created(location).build();
    }



    @GetMapping("/{post-id}")
    public ResponseEntity getPost(@PathVariable("post-id") @Positive long postId){
        Posts post = postsService.getPost(postId);

        PostsDto.ResponseOnPost response = postsMapper.postsToResponseOnPost(post);

        return new ResponseEntity<>(new SingleResponseDto<>(response),HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getPosts(@Positive @RequestParam int page,
                                   @Positive @RequestParam int size){
        Page<Posts> pagePosts = postsService.getPosts(page - 1, size);
        List<Posts> posts = pagePosts.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(postsMapper.postsToResponseOnBoards(posts),
                        pagePosts),
                HttpStatus.OK);
    }

    @GetMapping("/search/title")
    public ResponseEntity searchPosts(@Positive @RequestParam int page,
                                      @Positive @RequestParam int size,
                                      @RequestParam String keyword){

        Page<Posts> pagePosts = postsService.searchPosts(page-1,size,keyword);

        List<Posts> posts = pagePosts.getContent();

        return new ResponseEntity<>(
                new MultiResponseDto<>(postsMapper.postsToResponseOnBoards(posts),
                        pagePosts),
                HttpStatus.OK);
    }

    @DeleteMapping("/{post-id}")
    public ResponseEntity deletePost(@PathVariable("post-id") @Positive long postId){
        postsService.deletePost(postId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
