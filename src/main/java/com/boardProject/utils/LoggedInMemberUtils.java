package com.boardProject.utils;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class LoggedInMemberUtils {
    public static JwtVerificationFilter.AuthenticatedPrincipal findLoggedInMember(){
        var user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user instanceof JwtVerificationFilter.AuthenticatedPrincipal)
            return (JwtVerificationFilter.AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        else return null;
    }
    public static boolean verifyIsMineBoolean(long memberId){
        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = findLoggedInMember();
        try{
            return loggedInMember.getMemberId() == memberId;
        }catch (NullPointerException e){
            return false;
        }
    }

    public static void verifyIsMineException(long memberId){
        if(!verifyIsMineBoolean(memberId))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_AUTHORIZED);
    }
}
