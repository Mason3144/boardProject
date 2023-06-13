package com.boardProject.mockTest.board.comment.controller;


import com.boardProject.board.controller.CommentController;
import com.boardProject.board.controller.LikeController;
import com.boardProject.board.dto.CommentDto;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.mapper.CommentsMapper;
import com.boardProject.board.service.CommentService;
import com.boardProject.helper.StubData;
import com.boardProject.helper.comment.CommentControllerTestHelper;
import com.boardProject.helper.posts.PostsControllerTestHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = CommentController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class CommentControllerRestDocsTest implements CommentControllerTestHelper, PostsControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CommentService service;
    @MockBean
    private CommentsMapper mapper;


    @Test
    @WithMockUser
    public void postCommentTest() throws Exception {
        String content = toJsonContent(StubData.MockComment.getRequestBody(HttpMethod.POST));

        CommentDto.Response mockComment = StubData.MockComment.getResponseBody(HttpMethod.GET);
        given(mapper.commentPostDtoToComment(Mockito.any())).willReturn(new Comment());

        given(service.createComment(Mockito.any())).willReturn(new Comment());

        given(mapper.commentToCommentResponseDto(Mockito.any())).willReturn(mockComment);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("postid","1");

        ResultActions actions = mockMvc.perform(postRequestBuilder(getCommentUrl(), content, params));
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.commentId").value(mockComment.getCommentId()))
                .andExpect(jsonPath("$.data.comment").value(mockComment.getComment()))
                .andExpect(jsonPath("$.data.postId").value(mockComment.getPostId()))
                .andExpect(jsonPath("$.data.memberId").value(mockComment.getMemberId()))
                .andDo(document("post-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                parameterWithName("postid").description("게시글 식별자").attributes(key("constraints").value("0이상 정수")),
                                parameterWithName("_csrf").ignored()
                        ),
                        requestFields(
                                getCommentPostRequestDescriptor()
                        ),
                        responseFields(
                                getCommentResponseDescriptor()
                        )
                ));
    }

    @Test
    @WithMockUser
    public void patchCommentTest() throws Exception {
        long commentId = 1L;
        String content = toJsonContent(StubData.MockComment.getRequestBody(HttpMethod.PATCH));

        CommentDto.Response mockComment = StubData.MockComment.getResponseBody(HttpMethod.GET);

        given(service.updateComment(Mockito.any())).willReturn(new Comment());

        given(mapper.commentToCommentResponseDto(Mockito.any())).willReturn(mockComment);

        ResultActions actions = mockMvc.perform(patchRequestBuilder(getCommentUri(),commentId, content));
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.commentId").value(mockComment.getCommentId()))
                .andExpect(jsonPath("$.data.comment").value(mockComment.getComment()))
                .andExpect(jsonPath("$.data.postId").value(mockComment.getPostId()))
                .andExpect(jsonPath("$.data.memberId").value(mockComment.getMemberId()))
                .andDo(document("patch-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getCommentPathParameterDescriptor()
                        ),
                        requestFields(
                                getCommentPatchRequestDescriptor()
                        ),
                        responseFields(
                                getCommentResponseDescriptor()
                        )
                ));
    }

    @Test
    @WithMockUser
    public void deleteCommentTest() throws Exception {
        long commentId = 1L;

        ResultActions actions = mockMvc.perform(deleteRequestBuilder(getCommentUri(),commentId));
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-comment",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getCommentPathParameterDescriptor()
                        )
                ));
    }

}
