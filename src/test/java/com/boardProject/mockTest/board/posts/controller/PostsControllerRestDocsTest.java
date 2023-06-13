package com.boardProject.mockTest.board.posts.controller;


import com.boardProject.board.controller.PostsController;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Posts;
import com.boardProject.board.mapper.PostsMapper;
import com.boardProject.board.service.PostsService;
import com.boardProject.helper.StubData;
import com.boardProject.helper.posts.PostsControllerTestHelper;
import com.boardProject.member.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = PostsController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class PostsControllerRestDocsTest implements PostsControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostsService postsService;
    @MockBean
    private PostsMapper mapper;

    @Test
    @WithMockUser
    public void postPostTest() throws Exception {
        Posts mockResultPost = new Posts();
        mockResultPost.setPostId(1L);

        given(mapper.postDtoToPosts(Mockito.any(PostsDto.Post.class))).willReturn(new Posts());

        given(postsService.createPost(Mockito.any(Posts.class),Mockito.anyList())).willReturn(mockResultPost);


        String content = toJsonContent(StubData.MockPost.getRequestBody(HttpMethod.POST));

        ResultActions actions = mockMvc.perform(multipartRequestBuilder(getPostsUrl(),content));

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v1/posts"))))
                .andDo(document("post-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestPartFields("requestBody",
                                getPostsPostRequestDescriptor()
                                ),
                        requestPartBody("photoImgs"),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                        )
                ));
    }

    @Test
    @WithMockUser
    public void patchPostTest() throws Exception {
        long postId = 1L;
        Posts mockResult = new Posts();
        mockResult.setPostId(postId);

        given(mapper.patchDtoToPosts(Mockito.any(PostsDto.Patch.class))).willReturn(new Posts());

        given(postsService.updatePost(Mockito.any(Posts.class))).willReturn(mockResult);

        String content = toJsonContent(StubData.MockPost.getRequestBody(HttpMethod.PATCH));

        ResultActions actions = mockMvc.perform(patchRequestBuilder(getPostsUri(),postId, content));

        actions
                .andExpect(status().isOk())
                .andExpect(header().string("Location", is(startsWith("/v1/posts"))))
                .andDo(document("patch-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getPostsPathParameterDescriptor()
                        ),
                        requestFields(
                                getPostsPatchRequestDescriptor()
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                        )
                ));
    }

    @Test
    @WithMockUser
    public void getPostTest() throws Exception {
        long postId = 1L;

        PostsDto.ResponseOnPost response = StubData.MockPost.getResponseBodyOnPost(HttpMethod.GET);

        given(postsService.getPost(Mockito.anyLong())).willReturn(new Posts());

        given(mapper.postsToResponseOnPost(Mockito.any())).willReturn(response);

        ResultActions actions = mockMvc.perform(getRequestBuilder(getPostsUri(),postId));

        getPostsGetTestExpectation(actions,postId,response)
                .andDo(document("get-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getPostsPathParameterDescriptor()
                        ),
                        responseFields(
                                getPostsResponseOnPostDescriptor()
                        )
                ));
    }

    @Test
    @WithMockUser
    public void getPostsTest() throws Exception {
        String page = "1";
        String size = "10";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("page",page);
        params.add("size",size);

        List<PostsDto.ResponseOnBoard> response = StubData.MockPost.getResponseBodyOnBoard(HttpMethod.GET);

        Page<Posts> pagePosts = new PageImpl<>(List.of(new Posts(),new Posts()),
                PageRequest.of(0,10, Sort.by("postId").descending()),2);

        given(postsService.getPosts(Mockito.anyInt(),Mockito.anyInt())).willReturn(pagePosts);

        given(mapper.postsToResponseOnBoards(Mockito.any())).willReturn(response);


        ResultActions actions = mockMvc.perform(getRequestBuilder(getPostsUrl(),params));

        getPostsGetTestExpectation(actions,response)
                .andDo(document("get-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                getDefaultRequestParameterDescriptors()
                        ),
                        responseFields(
                                getPostsResponseOnBoardDescriptor()
                        )
                ));
    }

    @Test
    @WithMockUser
    public void searchPostsTest() throws Exception {
        String page = "1";
        String size = "10";
        String keyword = "keyword";

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("page",page);
        params.add("size",size);
        params.add("keyword", keyword);

        List<PostsDto.ResponseOnBoard> response = StubData.MockPost.getResponseBodyOnBoard(HttpMethod.GET);

        Page<Posts> pagePosts = new PageImpl<>(List.of(new Posts(),new Posts()),
                PageRequest.of(0,10, Sort.by("postId").descending()),2);

        given(postsService.searchPosts(Mockito.anyInt(),Mockito.anyInt(),Mockito.anyString())).willReturn(pagePosts);

        given(mapper.postsToResponseOnBoards(Mockito.any())).willReturn(response);


        ResultActions actions = mockMvc.perform(getRequestBuilder(getSearchUrl(),params));

        getPostsGetTestExpectation(actions,response)
                .andDo(document("search-posts",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestParameters(
                                getSearchRequestParameterDescriptors()
                        ),
                        responseFields(
                                getPostsResponseOnBoardDescriptor()
                        )
                ));
    }


    @Test
    @WithMockUser
    public void deletePostsTest() throws Exception {
        long postId = 1L;

        MemberDto.Response response = StubData.MockMember.getResponseBody(HttpMethod.DELETE);

        ResultActions actions = mockMvc.perform(deleteRequestBuilder(getPostsUri(),postId));

        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-post",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getPostsPathParameterDescriptor()
                        )
                ));
    }
}
