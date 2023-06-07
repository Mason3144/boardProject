package com.boardProject.emailVerification.repository;

import com.boardProject.emailVerification.entity.EmailVerification;
import com.boardProject.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification,Long> {

    // 인증에서 완료된 코드는 검색에서 제외한다.
    // 이유1. 만에하나 인증코드가 중복될 수도 있기에 최대한 중복될 확률을 줄일 수 있다.
    // 이유2. 혹시라도 인증코드로 사용자 정보를 탈취할 가능성도 있기에 인증완료된 코드는 만료를 시킴으로써 보안이 좀더 강화가 된다.

    // PasswordEncoder의 단방향 암호화를 사용하여 보안을 더 강화할 수 있지만 대부분의 사용자들은 회원가입과 동시에 이메일 인증을 하며
    // 인증이후 해당 인증코드는 만료가 되기때문에 불필요하다 판단
    @Query("select e, m from EmailVerification e join fetch e.member m where e.code = :code and e.verified = FALSE")
    Optional<EmailVerification> findByCode(String code);
}
