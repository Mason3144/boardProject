package com.codestates.auth.handler;

import com.codestates.response.ErrorResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MemberAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.error("# Authentication failed: {}", exception.getMessage());

        sendErrorResponse(response, exception);
    }

    private void sendErrorResponse(HttpServletResponse response, Exception exception) throws IOException {
        Gson gson = new Gson();     // (2-1)
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.UNAUTHORIZED, exception.getMessage()); // (2-2)

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);    // (2-3)
        response.setStatus(HttpStatus.UNAUTHORIZED.value());          // (2-4)

        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));   // (2-5)
    }
}
