package com.boardProject.utils;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedInMember {
    public static JwtVerificationFilter.AuthenticatedPrincipal findLoggedInMember(){
        if(isLoggedIn()){
            return (JwtVerificationFilter.AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return JwtVerificationFilter.AuthenticatedPrincipal.builder().isLoggedIn(false).build();
    }
    public static boolean isLoggedIn(){
        return !SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals(String.class);
    }
}
