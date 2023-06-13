package com.boardProject.helper.comment;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.helper.ControllerTestHelper;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

public interface CommentControllerTestHelper extends ControllerTestHelper {
    String COMMENT_URL = "/v1/comment";
    String COMMENT_URI = "/{comment-id}";
    default String getCommentUri(){
        return COMMENT_URL+COMMENT_URI;
    }
    default String getCommentUrl(){
        return COMMENT_URL;
    }

    default List<ParameterDescriptor> getCommentPathParameterDescriptor(){
        return List.of(parameterWithName("comment-id").description("댓글 식별자").attributes(key("constraints").value("0이상 정수")));
    }
    default List<FieldDescriptor> getCommentResponseDescriptor(){
        return List.of(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 객체"),
                fieldWithPath("data.postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                fieldWithPath("data.commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                fieldWithPath("data.comment").type(JsonFieldType.STRING).description("댓글 내용")
        );
    }

    default List<FieldDescriptor> getCommentPostRequestDescriptor(){
        ConstraintDescriptions postMemberConstraints = new ConstraintDescriptions(PostsDto.Post.class);
        List<String> contentConstraint = postMemberConstraints.descriptionsForProperty("content");

        return List.of(
                fieldWithPath("postId").type(JsonFieldType.STRING).description("게시글 식별자").ignored(),
                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용").optional().attributes(key("constraints").value(contentConstraint))
        );
    }

    default List<FieldDescriptor> getCommentPatchRequestDescriptor(){
        ConstraintDescriptions postMemberConstraints = new ConstraintDescriptions(PostsDto.Post.class);
        List<String> contentConstraint = postMemberConstraints.descriptionsForProperty("content");

        return List.of(
                fieldWithPath("commentId").type(JsonFieldType.STRING).description("댓글 식별자").ignored(),
                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용").optional().attributes(key("constraints").value(contentConstraint))
        );
    }

}
