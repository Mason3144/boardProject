package com.boardProject.member.repository;


import com.boardProject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    @Query("select m from Member m where m.memberId = :memberId and not (m.memberStatus = 'MEMBER_QUIT')")
    Optional<Member> findById(Long memberId);
}
