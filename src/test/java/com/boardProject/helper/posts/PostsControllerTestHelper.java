package com.boardProject.helper.posts;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.helper.ControllerTestHelper;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface PostsControllerTestHelper extends ControllerTestHelper {
    String POSTS_URL = "/v1/posts";
    String RESOURCE_URI = "/{post-id}";
    String SEARCH_URL = "/search/title";
    default String getPostsUrl(){
        return POSTS_URL;
    }
    default String getPostsUri(){
        return POSTS_URL + RESOURCE_URI;
    }
    default String getSearchUrl(){
        return POSTS_URL+SEARCH_URL;
    }

    default List<ParameterDescriptor> getPostsPathParameterDescriptor(){
        return List.of(parameterWithName("post-id").description("게시글 식별자 ID").attributes(key("constraints").value("0이상 정수")));
    }
    default ResultActions getPostsGetTestExpectation(ResultActions actions, long postId, PostsDto.ResponseOnPost response) throws Exception {
        return actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.postId").value(postId))
                .andExpect(jsonPath("$.data.title").value(response.getTitle()))
                .andExpect(jsonPath("$.data.views").value(response.getViews()))
                .andExpect(jsonPath("$.data.writer.memberId").value(response.getWriter().getMemberId()))
                .andExpect(jsonPath("$.data.content").value(response.getContent()))
                .andExpect(jsonPath("$.data.postStatus").value(response.getPostStatus().toString()))
                .andExpect(jsonPath("$.data.likes.totalLikes").value(response.getLikes().getTotalLikes()))
                .andExpect(jsonPath("$.data.comments").isArray())
                .andExpect(jsonPath("$.data.createdAt").value(response.getCreatedAt().toString()+":00"))
                .andExpect(jsonPath("$.data.photos").isArray())
                .andExpect(jsonPath("$.data.mine").value(response.isMine()));
    }

    default ResultActions getPostsGetTestExpectation(ResultActions actions, List<PostsDto.ResponseOnBoard> response) throws Exception {
        return actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageInfo.page").value(1))
                .andExpect(jsonPath("$.pageInfo.size").value(10))
                .andExpect(jsonPath("$.pageInfo.totalElements").value(2))
                .andExpect(jsonPath("$.pageInfo.totalPages").value(1))

                .andExpect(jsonPath("$.data[0].postId").value(response.get(0).getPostId()))
                .andExpect(jsonPath("$.data[1].postId").value(response.get(1).getPostId()));
    }

    default List<FieldDescriptor> getFullResponseDescriptors() {
        Stream<FieldDescriptor> defaultResponseDescriptors = getDefaultResponseDescriptors(JsonFieldType.OBJECT).stream();

        Stream<FieldDescriptor> defaultWriterResponseDescriptors = getDefaultWriterResponseDescriptors(DataResponseType.SINGLE).stream();
        Stream<FieldDescriptor> defaultPostsResponseDescriptors = getDefaultPostsResponseDescriptors(DataResponseType.SINGLE).stream();
        Stream<FieldDescriptor> defaultOnePostResponseDescriptors = getPostsResponseOnPostDescriptor().stream();

        return Stream.of(defaultResponseDescriptors, defaultWriterResponseDescriptors,defaultPostsResponseDescriptors,defaultOnePostResponseDescriptors)
                .flatMap(descriptorStream->descriptorStream)
                .collect(Collectors.toList());
    }

    default List<FieldDescriptor> getFullPageResponseDescriptors() {

        Stream<FieldDescriptor> defaultResponseDescriptors = getDefaultResponseDescriptors(JsonFieldType.ARRAY).stream();
        Stream<FieldDescriptor> pageResponseDescriptors = getPageResponseDescriptors().stream();

        Stream<FieldDescriptor> defaultWriterResponseDescriptors = getDefaultWriterResponseDescriptors(DataResponseType.LIST).stream();
        Stream<FieldDescriptor> defaultPostsResponseDescriptors = getDefaultPostsResponseDescriptors(DataResponseType.LIST).stream();
        Stream<FieldDescriptor> defaultOnBoardResponseDescriptors = getPostsResponseOnBoardDescriptor().stream();

        Stream<FieldDescriptor> mergedStream =
                Stream.of(defaultResponseDescriptors, defaultWriterResponseDescriptors, defaultPostsResponseDescriptors, defaultOnBoardResponseDescriptors, pageResponseDescriptors)
                        .flatMap(descriptorStream -> descriptorStream);
        return mergedStream.collect(Collectors.toList());
    }

    default List<FieldDescriptor> getPageResponseDescriptors() {
        return Arrays.asList(
                fieldWithPath("pageInfo").type(JsonFieldType.OBJECT).description("페이지 정보").optional(),
                fieldWithPath("pageInfo.page").type(JsonFieldType.NUMBER).description("페이지 번호").optional(),
                fieldWithPath("pageInfo.size").type(JsonFieldType.NUMBER).description("페이지 사이즈").optional(),
                fieldWithPath("pageInfo.totalElements").type(JsonFieldType.NUMBER).description("전체 건 수").optional(),
                fieldWithPath("pageInfo.totalPages").type(JsonFieldType.NUMBER).description("전체 페이지 수").optional()
        );
    }

    default List<FieldDescriptor> getDefaultResponseDescriptors(JsonFieldType jsonFieldTypeForData) {
        return List.of(
                fieldWithPath("data").type(jsonFieldTypeForData).description("결과 데이터")
        );
    }


    default List<FieldDescriptor> getDefaultWriterResponseDescriptors(DataResponseType dataResponseType) {
        String parentPath = getDataParentPath(dataResponseType);
        return List.of(
                fieldWithPath(parentPath.concat("writer")).type(JsonFieldType.OBJECT).description("글쓴이 데이터"),
                fieldWithPath(parentPath.concat("writer.memberId")).type(JsonFieldType.NUMBER).description("글쓴이 회원 식별자"),
                fieldWithPath(parentPath.concat("writer.email")).type(JsonFieldType.STRING).description("글쓴이 이메일"),
                fieldWithPath(parentPath.concat("writer.name")).type(JsonFieldType.STRING).description("글쓴이 이름"),
                fieldWithPath(parentPath.concat("writer.memberStatus")).type(JsonFieldType.STRING)
                        .description("글쓴이 회원 상태: MEMBER_ACTIVE(활동중) / MEMBER_SLEEP(휴면 계정) / MEMBER_QUIT(탈퇴)")

                );
    }
    default List<FieldDescriptor> getDefaultPostsResponseDescriptors(DataResponseType dataResponseType) {
        String parentPath = getDataParentPath(dataResponseType);
        return List.of(
                fieldWithPath(parentPath.concat("postId")).type(JsonFieldType.NUMBER).description("게시글 식별자"),
                fieldWithPath(parentPath.concat("title")).type(JsonFieldType.STRING).description("게시글 제목"),
                fieldWithPath(parentPath.concat("views")).type(JsonFieldType.NUMBER).description("조회수"),
                fieldWithPath(parentPath.concat("postStatus")).type(JsonFieldType.STRING)
                        .description("글 상태: POST_PUBLIC(공개글) / POST_PRIVATE(비밀글) / POST_DELETED(삭제된 글)"),
                fieldWithPath(parentPath.concat("createdAt")).type(JsonFieldType.STRING).description("글 생성 시간"),
                fieldWithPath(parentPath.concat("mine")).type(JsonFieldType.BOOLEAN).description("내가 작성한 글")
        );
    }

    default List<FieldDescriptor> getPostsResponseOnPostDescriptor(){
        return List.of(
                fieldWithPath("data.content").type(JsonFieldType.STRING).description("글 내용"),

                fieldWithPath("data.likes").type(JsonFieldType.OBJECT).description("좋아요 데이터"),
                fieldWithPath("data.likes.totalLikes").type(JsonFieldType.NUMBER).description("좋아요 개수"),
                fieldWithPath("data.likes.liked").type(JsonFieldType.BOOLEAN).description("개인 좋아요 확인"),

                fieldWithPath("data.comments").type(JsonFieldType.ARRAY).description("댓글 데이터"),
                fieldWithPath("data.comments[].commentId").type(JsonFieldType.NUMBER).description("댓글 식별자"),
                fieldWithPath("data.comments[].memberId").type(JsonFieldType.NUMBER).description("댓글 작성자 식별자"),
                fieldWithPath("data.comments[].postId").type(JsonFieldType.NUMBER).description("댓글 작성 게시글 식별자"),
                fieldWithPath("data.comments[].comment").type(JsonFieldType.STRING).description("댓글 내용"),

                fieldWithPath("data.photos").type(JsonFieldType.ARRAY).description("첨부 데이터"),
                fieldWithPath("data.photos[].photoId").type(JsonFieldType.NUMBER).description("첨부 사진 식별자"),
                fieldWithPath("data.photos[].filePath").type(JsonFieldType.STRING).description("첨부 사진 URL")
        );
    }

    default List<FieldDescriptor> getPostsResponseOnBoardDescriptor(){
        return List.of(
                fieldWithPath("data[].commentsNumber").type(JsonFieldType.NUMBER).description("댓글 개수")
        );
    }

    default List<FieldDescriptor> getPostsPostRequestDescriptor(){
        ConstraintDescriptions postMemberConstraints = new ConstraintDescriptions(PostsDto.Post.class);
        List<String> titleConstraint = postMemberConstraints.descriptionsForProperty("title");
        List<String> statusConstraint = postMemberConstraints.descriptionsForProperty("postStatus");
        List<String> contentConstraint = postMemberConstraints.descriptionsForProperty("content");

        return List.of(
                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목").attributes(key("constraints").value(titleConstraint)),
                fieldWithPath("postStatus").type(JsonFieldType.STRING).description("게시글 상태").attributes(key("constraints").value(statusConstraint)),
                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용").attributes(key("constraints").value(contentConstraint))
        );
    }
    default List<FieldDescriptor> getPostsPatchRequestDescriptor(){
        ConstraintDescriptions postMemberConstraints = new ConstraintDescriptions(PostsDto.Post.class);
        List<String> titleConstraint = postMemberConstraints.descriptionsForProperty("title");
        List<String> statusConstraint = postMemberConstraints.descriptionsForProperty("postStatus");
        List<String> contentConstraint = postMemberConstraints.descriptionsForProperty("content");

        return List.of(
                fieldWithPath("postId").type(JsonFieldType.STRING).description("게시글 식별자").ignored(),
                fieldWithPath("title").type(JsonFieldType.STRING).description("게시글 제목").optional().attributes(key("constraints").value(titleConstraint)),
                fieldWithPath("postStatus").type(JsonFieldType.STRING).description("게시글 상태").optional().attributes(key("constraints").value(statusConstraint)),
                fieldWithPath("content").type(JsonFieldType.STRING).description("게시글 내용").optional().attributes(key("constraints").value(contentConstraint))
        );
    }

    default String getDataParentPath(DataResponseType dataResponseType) {
        return dataResponseType == DataResponseType.SINGLE ? "data." : "data[].";
    }
    enum DataResponseType {
        SINGLE, LIST
    }
}
