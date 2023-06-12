package com.boardProject.helper;

import com.boardProject.board.dto.CommentDto;
import com.boardProject.board.dto.LikeDto;
import com.boardProject.board.dto.PhotoDto;
import com.boardProject.board.dto.PostsDto;
import com.boardProject.board.entity.Posts;
import com.boardProject.member.dto.MemberDto;
import com.boardProject.member.entity.Member;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StubData {
    public static class MockMember{
        private static final Map<HttpMethod, Object> stubRequestBody;
        private static final Map<HttpMethod, MemberDto.Response> stubResponseBody;
        static {
            stubRequestBody = new HashMap<>();
            stubRequestBody.put(HttpMethod.POST, MemberDto.Post.builder()
                    .email("test@email.com")
                    .name("memberName")
                    .password("Member12")
                    .build());
            stubRequestBody.put(HttpMethod.PATCH, MemberDto.Patch.builder()
                    .email("modified@email.com")
                    .name("modifiedName")
                    .password("Modified12")
                    .build());

            stubResponseBody = new HashMap<>();
            stubResponseBody.put(HttpMethod.PATCH, MemberDto.Response.builder()
                    .memberId(1L)
                    .email("modified@email.com")
                    .name("modifiedName")
                    .memberStatus(Member.MemberStatus.MEMBER_ACTIVE)
                    .build());
            stubResponseBody.put(HttpMethod.GET, MemberDto.Response.builder()
                    .memberId(1L)
                    .email("member@email.com")
                    .name("memberName")
                    .memberStatus(Member.MemberStatus.MEMBER_ACTIVE)
                    .build());
            stubResponseBody.put(HttpMethod.DELETE, MemberDto.Response.builder()
                    .memberId(1L)
                    .email("member@email.com")
                    .name("memberName")
                    .memberStatus(Member.MemberStatus.MEMBER_QUIT)
                    .build());
        }

        public static Object getRequestBody(HttpMethod httpMethod){
            return stubRequestBody.get(httpMethod);
        }
        public static MemberDto.Response getResponseBody(HttpMethod httpMethod){
            return stubResponseBody.get(httpMethod);
        }
    }
    public static class MockLike{
        private static final Map<HttpMethod, LikeDto.ResponseOnPost> stubResponseBodyOnPost;
        static{
            stubResponseBodyOnPost = new HashMap<>();
            stubResponseBodyOnPost.put(HttpMethod.GET, LikeDto.ResponseOnPost.builder()
                            .totalLikes(1)
                            .isLiked(true)
                    .build());
        }
        public static LikeDto.ResponseOnPost getResponseBody(HttpMethod httpMethod){
            return stubResponseBodyOnPost.get(httpMethod);
        }

    }

    public static class MockComment{
        private static final Map<HttpMethod, CommentDto.Response> stubResponseBody;
        static{
            stubResponseBody = new HashMap<>();
            stubResponseBody.put(HttpMethod.GET, CommentDto.Response.builder()
                                .commentId(1)
                                .memberId(1)
                                .postId(1)
                                .comment("Comment Text")
                                .build());
        }
        public static CommentDto.Response getResponseBody(HttpMethod httpMethod){
            return stubResponseBody.get(httpMethod);
        }

    }

    public static class MockPost{
        private static final Map<HttpMethod, Object> stubRequestBody;
        private static final Map<HttpMethod, PostsDto.ResponseOnPost> stubResponseBodyOnPost;
        private static final Map<HttpMethod, List<PostsDto.ResponseOnBoard>> stubResponseBodyOnBoard;

        static {
            stubRequestBody = new HashMap<>();
            stubRequestBody.put(HttpMethod.POST, PostsDto.Post.builder()
                    .title("Post Title")
                    .postStatus(Posts.PostStatus.POST_PUBLIC)
                    .content("Post Content Text")
                    .build()
            );


            stubRequestBody.put(HttpMethod.PATCH, PostsDto.Patch.builder()
                    .title("Modified Title")
                    .postId(1)
                    .postStatus(Posts.PostStatus.POST_PUBLIC)
                    .content("Modified Content Text")
                    .build());





            stubResponseBodyOnPost = new HashMap<>();
            stubResponseBodyOnPost.put(HttpMethod.GET, PostsDto.ResponseOnPost.builder()
                    .postId(1)
                    .title("Title")
                    .views(0)
                    .writer(MockMember.getResponseBody(HttpMethod.GET))
                    .content("Content Text")
                    .postStatus(Posts.PostStatus.POST_PUBLIC)
                    .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                    .likes(MockLike.getResponseBody(HttpMethod.GET))
                    .comments(List.of(MockComment.getResponseBody(HttpMethod.GET)))
                    .photos(List.of(PhotoDto.Response.builder().photoId(1).filePath("http://filepath").build()))
                    .isMine(true)
                    .build());



            stubResponseBodyOnBoard = new HashMap<>();
            stubResponseBodyOnBoard.put(HttpMethod.GET,
                    List.of(
                            PostsDto.ResponseOnBoard.builder()
                                    .postId(1)
                                    .title("Post Title 1")
                                    .views(0)
                                    .writer(MockMember.getResponseBody(HttpMethod.GET))
                                    .postStatus(Posts.PostStatus.POST_PUBLIC)
                                    .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                                    .commentsNumber(0)
                                    .isMine(true)
                                    .build(),
                            PostsDto.ResponseOnBoard.builder()
                                    .postId(2)
                                    .title("Post Title 2")
                                    .views(0)
                                    .writer(MockMember.getResponseBody(HttpMethod.GET))
                                    .postStatus(Posts.PostStatus.POST_PUBLIC)
                                    .createdAt(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                                    .commentsNumber(0)
                                    .isMine(true)
                                    .build())
                    );
        }
        public static MockMultipartFile getMockJsonFile(String content){
            return new MockMultipartFile("requestBody", "",
                    "application/json", content.getBytes());
        }
        public static MockMultipartFile getMockImgFile(){
            return new MockMultipartFile("photoImgs", "image.png", "image/png",
                    "<<Img data>>".getBytes());
        }
        public static Object getRequestBody(HttpMethod httpMethod){
            return stubRequestBody.get(httpMethod);
        }
        public static PostsDto.ResponseOnPost getResponseBodyOnPost(HttpMethod httpMethod){
            return stubResponseBodyOnPost.get(httpMethod);
        }
        public static List<PostsDto.ResponseOnBoard> getResponseBodyOnBoard(HttpMethod httpMethod){
            return stubResponseBodyOnBoard.get(httpMethod);
        }
    }

}
