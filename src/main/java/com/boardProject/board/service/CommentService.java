package com.boardProject.board.service;

import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.repository.CommentRepository;
import com.boardProject.board.repository.LikeRepository;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.LoggedInMemberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsService postsService;

    public CommentService(CommentRepository commentRepository, PostsService postsService) {
        this.commentRepository = commentRepository;
        this.postsService = postsService;
    }

    public void createComment(){

    }

    public void removeComment(){

    }

    public void updateComment(){

    }
}
