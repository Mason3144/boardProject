package com.boardProject.board.service;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.entity.Posts;

import com.boardProject.board.repository.PostsRepository;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import com.boardProject.member.service.MemberService;
import com.boardProject.utils.CustomBeanUtils;
import com.boardProject.utils.LoggedInMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return repository.save(posts);
    }

    public Posts updatePost(Posts posts){
        // photo update needed
        // 이전 컨텐츠 삭제해야됨

        Posts foundPost =  findExistsPost(posts.getPostId());

        verifyIsMine(foundPost.getMember().getMemberId());
        // 컨트롤러의 mapper에서 이미 로그인 사용자를 조회하여 매개변수 posts에 로그인사용자 정보를 삽입함
        // 이후 verifyIsMine에서 또 로그인 사용자를 조회하게됨, 가능하다면 리펙터링 오히려 더 복잡해지면 그냥 냅두기

        Posts updatedPost = customBeanUtils.copyNonNullProperties(posts,foundPost);

        return repository.save(updatedPost);
    }

    public Posts getPost(long postId){
        Posts post = findExistsPost(postId);

        // ismine check, 만약 비공개글인데 다른사용자가 접근할 경우
        // 삭제된 글일경우 JPQL을 이용하여 만들기

        post.setViews();

        return repository.save(post);
    }
    public Page<Posts> getPosts(int page, int size){
        // postStatus 확인하여 delete 인경우 제외시키기 ,jpql
        // n+1문제 해결

        return repository.findAll(PageRequest.of(page, size,
                Sort.by("postId").descending()));
    }

    public Posts searchPosts(){
        return null;
    }

    public void deletePost(long postId){
        // postStatus 확인하여 delete 인경우 제외시키기, jpql

        Posts post = findExistsPost(postId);
        post.setPostStatus(Posts.PostStatus.POST_DELETED);
        repository.save(post);
    }

    public Posts findExistsPost(long postId){
        Optional<Posts> optionalMember = repository.findById(postId);
        return optionalMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.POST_NOT_FOUND));
    }

    private void verifyIsMine(long memberId){
        // 멤버에서도 사용하므로 나중에 리펙터링
        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = LoggedInMember.findLoggedInMember();

        if(!loggedInMember.isLoggedIn() || memberId != loggedInMember.getMemberId()) throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_AUTHORIZED);
    }

}
