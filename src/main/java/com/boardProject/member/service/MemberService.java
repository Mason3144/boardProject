package com.boardProject.member.service;

import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import com.boardProject.utils.CustomBeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository repository;
    private final CustomBeanUtils<Member> customBeanUtils;

    public MemberService(MemberRepository repository, CustomBeanUtils<Member> customBeanUtils) {
        this.repository = repository;
        this.customBeanUtils = customBeanUtils;
    }

    public Member createMember(Member member){
        // email verification needed
        // social login create member needed

        existsEmailChecker(member.getEmail());
        return repository.save(member);
    }

    public Member updateMember(Member member){
        // profile photo update needed
        Member foundMember = findExistsMember(member.getMemberId());

        Member updateMember = customBeanUtils.copyNonNullProperties(member,foundMember);

        return repository.save(updateMember);
    }

    public Member findMember(long memberId){
        return findExistsMember(memberId);
    }

    public Member deleteMember(long memberId){
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
}
