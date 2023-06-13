package com.boardProject.helper.like;

import com.boardProject.board.dto.PostsDto;
import com.boardProject.helper.ControllerTestHelper;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface LikeControllerTestHelper extends ControllerTestHelper {
    String Like_URL = "/v1/like";
    String TOGGLE_URL = "/toggle";
    default String getToggleUrl(){
        return Like_URL+TOGGLE_URL;
    }

    default List<FieldDescriptor> getToggleLikeResponseDescriptor(){
        return List.of(
                fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터 객체"),
                fieldWithPath("data.postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                fieldWithPath("data.memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                fieldWithPath("data.liked").type(JsonFieldType.BOOLEAN).description("현재 좋아요 상태")
        );
    }

}
