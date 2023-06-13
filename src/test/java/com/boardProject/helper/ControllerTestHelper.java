package com.boardProject.helper;

import com.google.gson.Gson;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;

public interface ControllerTestHelper<T> {

    default RequestBuilder multipartRequestBuilder(String url,
                                              String content) {
        return  RestDocumentationRequestBuilders.multipart(url)
                .file(StubData.MockPost.getMockImgFile())
                .file(StubData.MockPost.getMockJsonFile(content))
                .accept(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    default RequestBuilder postRequestBuilder(String url,
                                              String content) {
        return  post(url)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }
    default RequestBuilder postRequestBuilder(String url, MultiValueMap<String, String> queryParams) {
        return post(url)
                .params(
                        queryParams
                )
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON);
    }
    default RequestBuilder postRequestBuilder(String url, String content, MultiValueMap<String, String> queryParams) {
        return post(url)
                .params(
                        queryParams
                )
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
    }


    default RequestBuilder patchRequestBuilder(String url, long resourceId, String content) {
        return patch(url, resourceId)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

    }

    default RequestBuilder patchRequestBuilder(String uri, String content) {
        return patch(uri)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

    }

    default RequestBuilder getRequestBuilder(String url, long resourceId) {
        return get(url, resourceId)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    default RequestBuilder getRequestBuilder(String uri) {
        return get(uri)
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder getRequestBuilder(String url, MultiValueMap<String, String> queryParams) {
        return get(url)
                .params(
                        queryParams
                )
                .accept(MediaType.APPLICATION_JSON);
    }

    default RequestBuilder deleteRequestBuilder(String url, long resourceId) {
        return delete(url, resourceId)
                .with(SecurityMockMvcRequestPostProcessors.csrf());
    }

    default RequestBuilder deleteRequestBuilder(String uri) {
        return delete(uri);
    }

    default String toJsonContent(T t) {
        Gson gson = new Gson();
        String content = gson.toJson(t);
        return content;
    }

    default List<ParameterDescriptor> getDefaultRequestParameterDescriptors() {
        List<ParameterDescriptor> list = new ArrayList<>();
        list.add(parameterWithName("page").description("Page 번호").attributes(key("constraints").value("0이상 정수")));
        list.add(parameterWithName("size").description("Page Size").attributes(key("constraints").value("0이상 정수")));
        return list;
    }
    default List<ParameterDescriptor> getSearchRequestParameterDescriptors() {
        List<ParameterDescriptor> pageInfo = getDefaultRequestParameterDescriptors();
        pageInfo.add(parameterWithName("keyword").description("keyword"));

        return pageInfo;
    }


}
