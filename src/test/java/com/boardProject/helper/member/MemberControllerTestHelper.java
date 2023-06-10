package com.boardProject.helper.member;

import com.boardProject.helper.ControllerTestHelper;
import com.boardProject.member.dto.MemberDto;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;

import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.*;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public interface MemberControllerTestHelper extends ControllerTestHelper {
    String MEMBER_URL = "/v1/members";
    String RESOURCE_URI = "/{member-id}";
    default String getMemberUrl(){
        return MEMBER_URL;
    }
    default String getMemberUri(){
        return MEMBER_URL + RESOURCE_URI;
    }

    default List<ParameterDescriptor> getMemberPathParameterDescriptor(){
        return List.of(parameterWithName("member-id").description("회원 식별자 ID").attributes(key("constraints").value("0이상 정수")));
    }
    default List<FieldDescriptor> getMemberPostRequestDescriptor(){
        ConstraintDescriptions postMemberConstraints = new ConstraintDescriptions(MemberDto.Post.class);
        List<String> emailConstraint = postMemberConstraints.descriptionsForProperty("email");
        List<String> nameConstraint = postMemberConstraints.descriptionsForProperty("name");
        List<String> passwordConstraint = postMemberConstraints.descriptionsForProperty("password");

        return List.of(
                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일").attributes(key("constraints").value(emailConstraint)),
                fieldWithPath("name").type(JsonFieldType.STRING).description("이름").attributes(key("constraints").value(nameConstraint)),
                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드").attributes(key("constraints").value(passwordConstraint))
        );
    }
    default List<FieldDescriptor> getMemberPatchRequestDescriptor(){
        ConstraintDescriptions postMemberConstraints = new ConstraintDescriptions(MemberDto.Patch.class);
        List<String> emailConstraint = postMemberConstraints.descriptionsForProperty("email");
        List<String> nameConstraint = postMemberConstraints.descriptionsForProperty("name");
        List<String> passwordConstraint = postMemberConstraints.descriptionsForProperty("password");

        return List.of(
                fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자").ignored(),
                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일").optional().attributes(key("constraints").value(emailConstraint)),
                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름").optional().attributes(key("constraints").value(nameConstraint)),
                fieldWithPath("password").type(JsonFieldType.STRING).description("회원 비밀번호").optional().attributes(key("constraints").value(passwordConstraint))
        );
    }
    default List<FieldDescriptor> getMemberResponseDescriptor(){
        return List.of(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("결과 데이터"),
                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                fieldWithPath("data.memberStatus").type(JsonFieldType.STRING)
                        .description("회원 상태: MEMBER_ACTIVE(활동중) / MEMBER_SLEEP(휴면 계정) / MEMBER_QUIT(탈퇴)")
        );
    }

}
