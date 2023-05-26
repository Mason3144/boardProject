package com.boardProject.utils;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedInMember {
    public static JwtVerificationFilter.AuthenticatedPrincipal findLoggedInMember(){
        return (JwtVerificationFilter.AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
