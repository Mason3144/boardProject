package com.boardProject.board.mapper;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Content;
import com.boardProject.board.entity.Posts;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.LoggedInMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostsMapper {

    default Posts postDtoToPosts(PostsDto.Post requestBody){
        Posts posts = Posts.builder()
                .postStatus(requestBody.getPostStatus())
                .title(requestBody.getTitle())
                .build();

        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = LoggedInMember.findLoggedInMember();
        Member member = new Member(loggedInMember.getMemberId());

        posts.setMember(member);
        posts.setContent(new Content(requestBody.getText()));

        return posts;
    };
    default PostsDto.ResponseOnPost postsToResponseOnPost(Posts posts){
        PostsDto.ResponseOnPost.builder()
                .postId(posts.getPostId())
                .title(posts.getTitle())
                .views(posts.getViews())
                .writer()
                .createdAt()
                .likes()
                .comments()
                .isMine()
                .build();
    };
}

