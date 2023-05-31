package com.boardProject.board.mapper;

import com.boardProject.board.dto.CommentDto;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Likes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LikeMapper {
    default LikeDto.ResponseOnToggle likesToLikeDto(Likes like){
        return LikeDto.ResponseOnToggle.builder()
                .memberId(like.getMember().getMemberId())
                .postId(like.getPosts().getPostId())
                .isLiked(like.getLikesId() != null)
                .build();
    }
}
