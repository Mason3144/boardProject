package com.boardProject.member.service;


import com.boardProject.auth.jwt.filter.JwtVerificationFilter;
import com.boardProject.auth.utils.CustomAuthorityUtils;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import com.boardProject.utils.CustomBeanUtils;
import com.boardProject.utils.LoggedInMember;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository repository;
    private final CustomBeanUtils<Member> customBeanUtils;
    private final CustomAuthorityUtils customAuthorityUtils;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository repository, CustomBeanUtils<Member> customBeanUtils, CustomAuthorityUtils customAuthorityUtils, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.customBeanUtils = customBeanUtils;
        this.customAuthorityUtils = customAuthorityUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public Member createMember(Member member){
        // email verification needed
        existsEmailChecker(member.getEmail());

        member.setRoles(customAuthorityUtils.createRoles(member.getEmail()));

        member.setPassword(passwordEncoder.encode(member.getPassword()));

        return repository.save(member);
    }

    public Member oauth2CreateMember(Member member){
        member.setRoles(customAuthorityUtils.createRoles(member.getEmail()));

        Optional<Member> optionalMember = repository.findByEmail(member.getEmail());
        return optionalMember.orElseGet(() -> optionalMember.orElse(repository.save(member)));

    }

    public Member updateMember(Member member){
        // profile photo update needed
        // when updating password, need to use password encoder
        verifyIsMine(member.getMemberId());

        Member foundMember = findExistsMember(member.getMemberId());

        Member updateMember = customBeanUtils.copyNonNullProperties(member,foundMember);

        return repository.save(updateMember);
    }

    public Member findMember(long memberId){
        verifyIsMine(memberId);
        // 삭제된 멤버일 경우

        return findExistsMember(memberId);
    }

    public Member deleteMember(long memberId){
        verifyIsMine(memberId);

        Member member = findExistsMember(memberId);
        member.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);

        return repository.save(member);
    }

    private void verifyIsMine(long memberId){
        // 포스트에서도 사용하므로 나중에 리펙터링
        long authenticatedMemberId = LoggedInMember.findLoggedInMember().getMemberId();
        if(memberId != authenticatedMemberId) throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_AUTHORIZED);
    }

    private void existsEmailChecker(String email){
        Optional<Member> member = repository.findByEmail(email);
        if(member.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    public Member findExistsMember(long memberId){
        Optional<Member> optionalMember = repository.findById(memberId);
        return optionalMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}
