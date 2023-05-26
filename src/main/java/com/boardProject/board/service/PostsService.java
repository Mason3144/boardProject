package com.boardProject.board.service;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.entity.Posts;

import com.boardProject.board.repository.PostsRepository;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import com.boardProject.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional

public class PostsService {
    private final PostsRepository repository;

    public PostsService(PostsRepository repository) {
        this.repository = repository;
    }

    public Posts createPost(Posts posts) {
        return repository.save(posts);
    }

    public Posts updatePost(){
        return null;
    }

    public Posts getPost(long postId){
        Posts post = findExistsPost(postId);

        // ismine check, 만약 비공개글인데 다른사용자가 접근할 경우
        // 삭제된 글일경우

        post.setViews();



        return repository.save(post);
    }

    public Posts getPosts(){
        return null;
    }

    public Posts searchPosts(){
        return null;
    }

    public Posts deletePost(){
        return null;
    }

    public Posts findExistsPost(long postId){
        Optional<Posts> optionalMember = repository.findById(postId);
        return optionalMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
    }

}
