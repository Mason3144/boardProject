package com.boardProject.auth.userDetailsService;

import com.boardProject.auth.utils.CustomAuthorityUtils;
import com.boardProject.exception.businessLogicException.BusinessLogicException;
import com.boardProject.exception.businessLogicException.ExceptionCode;
import com.boardProject.member.entity.Member;
import com.boardProject.member.repository.MemberRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository repository;
    private final CustomAuthorityUtils customAuthorityUtils;

    public MemberDetailsService(MemberRepository repository, CustomAuthorityUtils customAuthorityUtils) {
        this.repository = repository;
        this.customAuthorityUtils = customAuthorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = repository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        checkMemberStatus(findMember);

        return new MemberDetails(findMember);
    }
    private void checkMemberStatus(Member findMember){
        if(findMember.getMemberStatus() == Member.MemberStatus.MEMBER_SLEEP) throw new BusinessLogicException(ExceptionCode.MEMBER_SLEEP);
        else if(findMember.getMemberStatus() == Member.MemberStatus.MEMBER_QUIT) throw new BusinessLogicException(ExceptionCode.MEMBER_QUIT);
    }

    private final class MemberDetails extends Member implements UserDetails{
        public MemberDetails(Member member) {
            setMemberId(member.getMemberId());
            setEmail(member.getEmail());
            setName(member.getName());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return customAuthorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}