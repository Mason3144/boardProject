package com.boardProject.board.mapper;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Content;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.member.dto.MemberDto;
import com.boardProject.member.entity.Member;
import com.boardProject.member.mapper.MemberMapper;
import com.boardProject.utils.LoggedInMember;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostsMapper {
    MemberDto.Response memberToMemberResponse(Member member);

    // 싹다 리펙터링
    // postDtoToPosts 와 중복코드 많음
    default Posts patchDtoToPosts(PostsDto.Patch requestBody){
        Posts posts = Posts.builder()
                .postStatus(requestBody.getPostStatus())
                .title(requestBody.getTitle())
                .postId(requestBody.getPostId())
                .build();

        // 다른 dto들 처럼 비로그인 유저가 접근시 예외 발생하도록 변경 필요
        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = LoggedInMember.findLoggedInMember();
        Member member = new Member(loggedInMember.getMemberId());

        posts.setMember(member);
        posts.setContent(new Content(requestBody.getText()));

        return posts;
    }

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
        Member postOwner = posts.getMember();

        LikeDto.Response likeResponse = checkIsLiked(posts.getLikes());

        return PostsDto.ResponseOnPost.builder()
                .postId(posts.getPostId())
                .title(posts.getTitle())
                .views(posts.getViews())
                .writer(memberToMemberResponse(postOwner))
                .createdAt(posts.getCreatedAt())
                .likes(likeResponse)
                .content(posts.getContent().getContent())
                .comments(new LinkedList<>()) //  comment 로직 추가 필요
                .isMine(checkIsMine(postOwner.getMemberId()))
                .postStatus(posts.getPostStatus())
                .build();
    }

    default PostsDto.ResponseOnBoard postToResponseOnBoard(Posts posts){
        Member postOwner = posts.getMember();

        return PostsDto.ResponseOnBoard.builder()
                .postId(posts.getPostId())
                .title(posts.getTitle())
                .views(posts.getViews())
                .writer(memberToMemberResponse(postOwner))
                .postStatus(posts.getPostStatus())
                .createdAt(posts.getCreatedAt())
                .commentsNumber(0) // comment 로직 추가 필요
                .isMine(checkIsMine(postOwner.getMemberId()))
                .build();
    }

    default boolean checkIsMine(long writerId){
        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = LoggedInMember.findLoggedInMember();
        return loggedInMember.isLoggedIn() && (long) loggedInMember.getMemberId() == writerId;
    }
    default LikeDto.Response checkIsLiked(List<Likes> likesList){
        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = LoggedInMember.findLoggedInMember();

        Optional<Likes> isLiked = likesList.stream().filter(like->
                loggedInMember.isLoggedIn() && like.getMember().getMemberId() == (long) loggedInMember.getMemberId()
        ).findFirst();

        return LikeDto.Response.builder().totalLikes(likesList.size()).isLiked(isLiked.isPresent()).build();
    }


    List<PostsDto.ResponseOnBoard> postsToResponseOnBoards(List<Posts> posts);
}

