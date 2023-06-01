package com.boardProject.board.service;

import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.repository.CommentRepository;
import com.boardProject.board.repository.LikeRepository;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.CustomBeanUtils;
import com.boardProject.utils.LoggedInMemberUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostsService postsService;
    private final CustomBeanUtils<Comment> customBeanUtils;

    public CommentService(CommentRepository commentRepository, PostsService postsService, CustomBeanUtils<Comment> customBeanUtils) {
        this.commentRepository = commentRepository;
        this.postsService = postsService;
        this.customBeanUtils = customBeanUtils;
    }

    public Comment createComment(Comment comment){
        postsService.findExistsPost(comment.getPosts().getPostId());

        return commentRepository.save(comment);
    }

    public void removeComment(long commentId){
        Comment foundComment = findExistsComment(commentId);
        LoggedInMemberUtils.verifyIsMineException(foundComment.getMember().getMemberId());
        commentRepository.delete(foundComment);
    }

    public Comment updateComment(Comment comment){
        Comment foundComment = findExistsComment(comment.getCommentId());
        LoggedInMemberUtils.verifyIsMineException(foundComment.getMember().getMemberId());

        Comment newComment = customBeanUtils.copyNonNullProperties(comment,foundComment);

        return commentRepository.save(newComment);
    }

    public Comment findExistsComment(long commentId){
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        return optionalComment.orElseThrow(()->new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));
    }
}
