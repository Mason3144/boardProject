package com.boardProject.helper;

import com.boardProject.member.dto.MemberDto;
import com.boardProject.member.entity.Member;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
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
}
