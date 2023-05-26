package com.boardProject.board.service;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.entity.Posts;

import com.boardProject.board.repository.PostsRepository;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import com.boardProject.member.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class PostsService {
    private final PostsRepository postsRepository;
    private final MemberService memberService;

    public PostsService(PostsRepository postsRepository, MemberService memberService) {
        this.postsRepository = postsRepository;
        this.memberService = memberService;
    }

    public Posts createPost(Posts posts){

        int loggedInMember = findLoginMember().getMemberId();

         // 방법1. 현재 로그인된 사용자의 id를 이용하여 DB에서 해당 사용자를 찾은뒤 생성하려는 게시글과 연관관계 매핑을 한다.
        // 장점. 게시글을 통해 작성자의 모든 정보를 얻을수 있음, 단점. 게시글 생성시 작성자의 정보를 불러오기위해 한번더 DB에 접근해야함
//        Member member = memberService.findExistsMember(loggedInMember);


        // 방법2. 멤버 클래스를 새로만들어 현재 로그인된 사용자의 id만 부여하고 생성하려는 게시글과 연관관계 매핑을 한다.
        // 장점. 게시글 등록시 한번의 DB접근이면 가능, 단점. 게시글을 통해 작성자의 정보를 얻을시 DB에 접근하여야함, 완벽한 연관관계 매핑이 아님
        Member member = new Member();
        member.setMemberId((long) loggedInMember);



        member.setPosts(posts);




        Posts data = postsRepository.save(posts);
        System.out.println(data.getMember().getAttributes());

        Posts data2 = postsRepository.findById(1l).get();
        System.out.println(data2.getMember().getAttributes());

        return null;
    }

    public Posts updatePost(){
        return null;
    }

    public Posts getPost(){
        Posts data2 = postsRepository.findById(1l).get();
        System.out.println(data2.getMember().getAttributes());
        return null;
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

    public JwtVerificationFilter.AuthenticatedPrincipal findLoginMember(){
        return (JwtVerificationFilter.AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
