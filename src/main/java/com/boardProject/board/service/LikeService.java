package com.boardProject.board.service;

import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.repository.LikeRepository;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.LoggedInMemberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostsService postsService;

    public LikeService(LikeRepository likeRepository, PostsService postsService) {
        this.likeRepository = likeRepository;
        this.postsService = postsService;
    }

    public Likes toggleLike(long postId){
        Posts foundPost = postsService.findExistsPost(postId);
        int memberId = LoggedInMemberUtils.findLoggedInMember().getMemberId();

        Optional<Likes> like = foundPost.getLikes().stream().filter(l->l.getMember().getMemberId()==memberId).findFirst();

        return like.map(this::removeLike).orElseGet(() -> createLike(memberId, foundPost));
    }

    private Likes createLike(int memberId, Posts foundPost){
        Likes like = new Likes();
        Posts post = new Posts(foundPost.getPostId());
        Member member = new Member(memberId);

        post.setLikes(like);
        member.setLikes(like);

        return likeRepository.save(like);
    }

    private Likes removeLike(Likes like){
        likeRepository.delete(like);

        like.setLikesId(null);
        return like;
    }
}
