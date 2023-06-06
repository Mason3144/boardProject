package com.boardProject.emailVerification.controller;

import com.boardProject.auth.handler.MemberAuthenticationSuccessHandler;
import com.boardProject.emailVerification.service.EmailVerificationService;
import com.boardProject.member.entity.Member;
import com.boardProject.utils.UriCreator;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.function.EntityResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/v1/email")
public class EmailVerificationController {
    private final EmailVerificationService service;
    private final MemberAuthenticationSuccessHandler authenticationHandler;

    public EmailVerificationController(EmailVerificationService service, MemberAuthenticationSuccessHandler authenticationHandler) {
        this.service = service;
        this.authenticationHandler = authenticationHandler;
    }

    // JWT발행을 위해 HttpServletResponse를 매개변수로 받음
    // service 레이어를 거친 뒤 Member 클래스를 받아와 createAndApplyTokens()메서드를 사용하여 사용자 인증(로그인)
    @GetMapping("/verification")
    public void postEmailVerification(@RequestParam String code, HttpServletResponse response) throws IOException {
        Member member = service.verifyEmail(code);

        authenticationHandler.createAndApplyTokens(response,member);
    }
}
