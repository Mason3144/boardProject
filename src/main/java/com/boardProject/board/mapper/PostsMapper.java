package com.boardProject.board.mapper;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.dto.PostsDto;
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
public interface PostsMapper {
    MemberDto.Response memberToMemberResponse(Member member);
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
                .isMine(LoggedInMemberUtils.verifyIsMineBoolean(postOwner.getMemberId()))
                .build();
    }

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
                .content(posts.getContent())
                .comments(new LinkedList<>()) //  comment 로직 추가 필요
                .isMine(LoggedInMemberUtils.verifyIsMineBoolean(postOwner.getMemberId()))
                .postStatus(posts.getPostStatus())
                .build();
    }
    default LikeDto.Response checkIsLiked(List<Likes> likesList){
        Optional<Likes> isLiked = likesList.stream().filter(like->
                        LoggedInMemberUtils.verifyIsMineBoolean(like.getMember().getMemberId())
        ).findFirst();

        return LikeDto.Response.builder().totalLikes(likesList.size()).isLiked(isLiked.isPresent()).build();
    }

    Posts patchDtoToPosts(PostsDto.Patch requestBody);

    Posts postDtoToPosts(PostsDto.Post requestBody);

    List<PostsDto.ResponseOnBoard> postsToResponseOnBoards(List<Posts> posts);
}

