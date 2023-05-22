package com.boardProject.auth.handler;

import com.boardProject.auth.dto.LoginDto;
import com.boardProject.exception.errorResponse.ErrorResponse;
import com.boardProject.member.entity.Member;
import com.google.gson.Gson;
import jdk.jfr.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// response body를 이용하여 클라이언트에 필요한 정보 전달
@Slf4j
public class MemberAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authResult) throws IOException, ServletException {
        Member member = (Member) authResult.getPrincipal();
        Gson gson = new Gson();

        LoginDto.Response responseObj = LoginDto.Response.builder().email(member.getEmail()).name(member.getName()).build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(responseObj, LoginDto.Response.class));
    }
}