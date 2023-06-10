package com.boardProject.mockTest.member.controller;


import com.boardProject.helper.StubData;
import com.boardProject.helper.member.MemberControllerTestHelper;
import com.boardProject.member.controller.MemberController;
import com.boardProject.member.dto.MemberDto;
import com.boardProject.member.entity.Member;
import com.boardProject.member.mapper.MemberMapper;
import com.boardProject.member.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class MemberControllerRestDocsTest implements MemberControllerTestHelper {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberMapper mapper;
    @Autowired
    private Gson gson;

    @Test
    @WithMockUser
    public void postMemberTest() throws Exception {
        MemberDto.Post post = (MemberDto.Post) StubData.MockMember.getRequestBody(HttpMethod.POST);

        String content = toJsonContent(post);

        given(mapper.memberPostToMember(Mockito.any(MemberDto.Post.class))).willReturn(new Member());

        Member mockResultMember = new Member();
        mockResultMember.setMemberId(1L);

        given(memberService.createMember(Mockito.any(Member.class))).willReturn(mockResultMember);

        ResultActions actions = mockMvc.perform(postRequestBuilder(getMemberUrl(),content));

        actions
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(startsWith("/v1/members"))))
                .andDo(document("post-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                getMemberPostRequestDescriptor()
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location header. 등록된 리소스의 URI")
                        )
                ));
    }

    @Test
    @WithMockUser
    public void patchMemberTest() throws Exception {
        long memberId = 1L;
        MemberDto.Patch patch = (MemberDto.Patch) StubData.MockMember.getRequestBody(HttpMethod.PATCH);

        String content = toJsonContent(patch);

        MemberDto.Response response =  StubData.MockMember.getResponseBody(HttpMethod.PATCH);


        given(mapper.memberPatchToMember(Mockito.any(MemberDto.Patch.class))).willReturn(new Member());

        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());

        given(mapper.memberToMemberResponse(Mockito.any())).willReturn(response);

        ResultActions actions = mockMvc.perform(patchRequestBuilder(getMemberUri(), memberId, content));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(memberId))
                .andExpect(jsonPath("$.data.email").value(patch.getEmail()))
                .andExpect(jsonPath("$.data.memberStatus").value("MEMBER_ACTIVE"))
                .andDo(document("patch-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getMemberPathParameterDescriptor()
                        ),
                        requestFields(
                                getMemberPatchRequestDescriptor()
                        ),
                        responseFields(
                                getMemberResponseDescriptor()
                        )
                ));
    }

    @Test
    @WithMockUser
    public void getMemberTest() throws Exception {
        long memberId = 1L;

        MemberDto.Response response = StubData.MockMember.getResponseBody(HttpMethod.GET);

        given(memberService.findMember(Mockito.anyLong())).willReturn(new Member());

        given(mapper.memberToMemberResponse(Mockito.any())).willReturn(response);

        ResultActions actions = mockMvc.perform(getRequestBuilder(getMemberUri(),memberId));

        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(memberId))
                .andExpect(jsonPath("$.data.email").value(response.getEmail()))
                .andExpect(jsonPath("$.data.memberStatus").value("MEMBER_ACTIVE"))
                .andDo(document("get-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getMemberPathParameterDescriptor()
                        ),
                        responseFields(
                                getMemberResponseDescriptor()
                        )
                ));
    }

    @Test
    @WithMockUser
    public void deleteMemberTest() throws Exception {
        long memberId = 1L;

        MemberDto.Response response = StubData.MockMember.getResponseBody(HttpMethod.DELETE);

        given(memberService.deleteMember(Mockito.anyLong())).willReturn(new Member());

        given(mapper.memberToMemberResponse(Mockito.any())).willReturn(response);

        ResultActions actions = mockMvc.perform(deleteRequestBuilder(getMemberUri(),memberId));


        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.memberId").value(memberId))
                .andExpect(jsonPath("$.data.email").value(response.getEmail()))
                .andExpect(jsonPath("$.data.memberStatus").value("MEMBER_QUIT"))
                .andDo(document("delete-member",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                getMemberPathParameterDescriptor()
                        ),
                        responseFields(
                                getMemberResponseDescriptor()
                        )
                ));
    }
}
