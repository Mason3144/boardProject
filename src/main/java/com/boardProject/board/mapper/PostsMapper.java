package com.boardProject.board.mapper;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Content;
import com.boardProject.board.entity.Posts;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostsMapper {

    default Posts postDtoToPosts(PostsDto.Post requestBody){
        Posts posts = Posts.builder()
                .postStatus(requestBody.getPostStatus())
                .title(requestBody.getTitle())
                .build();

        posts.setContent(new Content(requestBody.getText()));
        return posts;
    };

}