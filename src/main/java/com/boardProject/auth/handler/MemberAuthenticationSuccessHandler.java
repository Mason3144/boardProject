package com.boardProject.auth.handler;

import com.boardProject.auth.dto.LoginDto;
import com.boardProject.auth.jwt.JwtTokenizer;
import com.boardProject.member.entity.Member;
import com.boardProject.member.service.MemberService;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

// response body를 이용하여 클라이언트에 필요한 정보 전달
@Slf4j
@Component
public class MemberAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtTokenizer jwtTokenizer;
    private final MemberService memberService;

    public MemberAuthenticationSuccessHandler(JwtTokenizer jwtTokenizer, MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.memberService = memberService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authResult) throws IOException, ServletException {

        Member member = getMember(authResult);

        createAndApplyTokens(response, member);
    }

    // 이메일 인증시 JWT발행을 위해 메서드를 분리
    public void createAndApplyTokens(HttpServletResponse response, Member member) throws IOException {
        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);

        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);


        Gson gson = new Gson();

        LoginDto.Response responseObj = LoginDto.Response.builder().email(member.getEmail()).name(member.getName()).build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(gson.toJson(responseObj, LoginDto.Response.class));
    }


    private Member getMember(Authentication authResult){
        var user = authResult.getPrincipal();

        if(user instanceof OAuth2User){
            return oAuth2CreateMember((OAuth2User) user);
        }else if(user instanceof Member){
            return (Member) user;
        }

        return new Member();
    }

    public Member oAuth2CreateMember(OAuth2User oAuth2User){
        Member member = new Member();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        member.setEmail(email);
        member.setName(name);
        member.setSocialLogin(true);

        return memberService.oauth2CreateMember(member);
    }

    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());
        claims.put("name", member.getName());
        claims.put("memberId", member.getMemberId());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }

    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
}