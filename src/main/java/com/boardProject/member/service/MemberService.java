package com.boardProject.member.service;


import com.boardProject.auth.utils.CustomAuthorityUtils;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import com.boardProject.utils.CustomBeanUtils;
import com.boardProject.utils.LoggedInMemberUtils;
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

        encodePassword(member);

        return repository.save(member);
    }

    public Member oauth2CreateMember(Member member){
        member.setRoles(customAuthorityUtils.createRoles(member.getEmail()));

        Optional<Member> optionalMember = repository.findByEmail(member.getEmail());
        return optionalMember.orElseGet(() -> optionalMember.orElse(repository.save(member)));

    }

    public Member updateMember(Member member){
        // profile photo update needed
        LoggedInMemberUtils.verifyIsMineException(member.getMemberId());
        encodePassword(member);

        Member foundMember = findExistsMember(member.getMemberId());

        Member updateMember = customBeanUtils.copyNonNullProperties(member,foundMember);

        return repository.save(updateMember);
    }

    public Member findMember(long memberId){
        LoggedInMemberUtils.verifyIsMineException(memberId);
        return findExistsMember(memberId);
    }

    public Member deleteMember(long memberId){
        LoggedInMemberUtils.verifyIsMineException(memberId);

        Member member = findExistsMember(memberId);
        member.setMemberStatus(Member.MemberStatus.MEMBER_QUIT);

        return repository.save(member);
    }

    private void existsEmailChecker(String email){
        Optional<Member> member = repository.findByEmail(email);
        if(member.isPresent()) throw new BusinessLogicException(ExceptionCode.MEMBER_EXISTS);
    }

    private Member findExistsMember(long memberId){
        Optional<Member> optionalMember = repository.findById(memberId);
        return optionalMember.orElseThrow(()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    private void encodePassword(Member member){
        if(member.getPassword() != null)
            member.setPassword(passwordEncoder.encode(member.getPassword()));
    }
}
