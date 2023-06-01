package com.boardProject.board.service;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.entity.Posts;

import com.boardProject.board.repository.PostsRepository;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.CustomBeanUtils;
import com.boardProject.utils.LoggedInMemberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional

public class PostsService {
    private final PostsRepository repository;
    private final CustomBeanUtils<Posts> customBeanUtils;

    public PostsService(PostsRepository repository, CustomBeanUtils<Posts> customBeanUtils) {
        this.repository = repository;
        this.customBeanUtils = customBeanUtils;
    }

    public Posts createPost(Posts posts) {
        // img file upload
        posts.setMember(new Member(LoggedInMemberUtils.findLoggedInMember().getMemberId()));
        return repository.save(posts);
    }

    public Posts updatePost(Posts posts){
        // photo update needed

        Posts foundPost =  findExistsPost(posts.getPostId());

        LoggedInMemberUtils.verifyIsMineException(foundPost.getMember().getMemberId());

        Posts newPost = customBeanUtils.copyNonNullProperties(posts,foundPost);

        return repository.save(newPost);
    }

    public Posts getPost(long postId){
        Posts foundPost = findExistsPost(postId);

        checkPrivatePost(foundPost);

        foundPost.setViews();

        foundPost.getLikes();

        return repository.save(foundPost);
    }
    public Page<Posts> getPosts(int page, int size){
        // postStatus 확인하여 delete 인경우 제외시키기 ,jpql
        // n+1문제 해결

        return repository.findAll(PageRequest.of(page, size,
                Sort.by("postId").descending()));
    }

    public Page<Posts> searchPosts(int page, int size, String keyword){

        Pageable pageable = PageRequest.of(page, size,
                Sort.by("postId").descending());

        return repository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public void deletePost(long postId){
        Posts foundPost = findExistsPost(postId);
        LoggedInMemberUtils.verifyIsMineException(foundPost.getMember().getMemberId());

        foundPost.setPostStatus(Posts.PostStatus.POST_DELETED);
        repository.save(foundPost);
    }

    public Posts findExistsPost(long postId){
        Optional<Posts> optionalPost = repository.findById(postId);
        return optionalPost.orElseThrow(()->new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
    }
    private void checkPrivatePost(Posts foundPost){
        if(foundPost.getPostStatus().equals(Posts.PostStatus.POST_PRIVATE))
            LoggedInMemberUtils.verifyIsMineException(foundPost.getMember().getMemberId());
    }
}
