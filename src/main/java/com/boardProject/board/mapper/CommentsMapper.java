package com.boardProject.board.mapper;

import com.boardProject.board.dto.CommentDto;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.member.dto.MemberDto;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.LoggedInMemberUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentsMapper {
    default Comment commentPostDtoToComment(CommentDto.Post postDto){
        int loggedInMemberId = LoggedInMemberUtils.findLoggedInMember().getMemberId();

        Comment comment = new Comment();
        comment.setContent(postDto.getContent());

        Member member = new Member(loggedInMemberId);
        member.setComments(comment);

        Posts post = new Posts(postDto.getPostId());
        post.setComments(comment);

        return comment;
    };
    Comment commentPatchDtoToComment(CommentDto.Patch patchDto);


    default CommentDto.Response commentToCommentResponseDto(Comment comment){
        return CommentDto.Response.builder()
                .commentId(comment.getCommentId())
                .comment(comment.getContent())
                .postId(comment.getPosts().getPostId())
                .memberId(comment.getMember().getMemberId())
                .build();
    }
}
