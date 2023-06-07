package com.boardProject.emailVerification.service;

import com.boardProject.auth.utils.CustomAuthorityUtils;
import com.boardProject.emailVerification.entity.EmailVerification;
import com.boardProject.emailVerification.repository.EmailVerificationRepository;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmailVerificationService {
    private final EmailVerificationRepository repository;

    // 회원의 권한부여를 위한 DI
    private final CustomAuthorityUtils customAuthorityUtils;

    public EmailVerificationService(EmailVerificationRepository repository, CustomAuthorityUtils customAuthorityUtils) {
        this.repository = repository;
        this.customAuthorityUtils = customAuthorityUtils;
    }


    public Member verifyEmail(String code){
        EmailVerification verification = findExistsVerification(code);
        verification.setVerified(true);
        Member member = verification.getMember();

        // 권한지정을 회원생성에서 이메일 인증으로 옴김
        member.setRoles(customAuthorityUtils.createRoles(member.getEmail()));

        EmailVerification test = repository.save(verification);
        return member;
    }
    private EmailVerification findExistsVerification(String code){
        Optional<EmailVerification> optionalMember = repository.findByCode(code);
        return optionalMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.VERIFICATION_NOT_FOUND));
    }
}
