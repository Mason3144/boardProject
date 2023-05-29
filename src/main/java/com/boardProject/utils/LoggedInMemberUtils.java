package com.boardProject.utils;

import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoggedInMemberUtils {
    public static JwtVerificationFilter.AuthenticatedPrincipal findLoggedInMember(){
        return (JwtVerificationFilter.AuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static boolean verifyIsMineBoolean(long memberId){
        JwtVerificationFilter.AuthenticatedPrincipal loggedInMember = findLoggedInMember();
        return loggedInMember.getMemberId() == memberId;
    }

    public static void verifyIsMineException(long memberId){
        if(verifyIsMineBoolean(memberId))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_AUTHORIZED);
    }
}
