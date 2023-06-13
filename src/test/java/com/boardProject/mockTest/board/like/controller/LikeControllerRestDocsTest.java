package com.boardProject.mockTest.board.like.controller;


import com.boardProject.board.controller.LikeController;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.mapper.LikeMapper;
import com.boardProject.board.service.LikeService;
import com.boardProject.helper.StubData;
import com.boardProject.helper.like.LikeControllerTestHelper;
import com.boardProject.helper.posts.PostsControllerTestHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = LikeController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class LikeControllerRestDocsTest implements LikeControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LikeService service;
    @MockBean
    private LikeMapper mapper;


    @Test
    @WithMockUser
    public void postLikeOnTest() throws Exception {
        LikeDto.ResponseOnToggle mockLike = StubData.MockLike.stubResponseBody(HttpMethod.POST);

        given(service.toggleLike(Mockito.anyLong())).willReturn(new Likes());

        given(mapper.likesToLikeDto(Mockito.any())).willReturn(mockLike);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("postid","1");

        ResultActions actions = mockMvc.perform(postRequestBuilder(getToggleUrl(),params));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.postId").value(mockLike.getPostId()))
                .andExpect(jsonPath("$.data.memberId").value(mockLike.getMemberId()))
                .andExpect(jsonPath("$.data.liked").value(mockLike.isLiked()))
                .andDo(document("post-like",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                List.of(
                                        parameterWithName("postid").description("게시글 식별자").attributes(key("constraints").value("0이상 정수")),
                                        parameterWithName("_csrf").ignored()
                                )),
                        responseFields(
                                        getToggleLikeResponseDescriptor()
                        )
                ));
    }
}
