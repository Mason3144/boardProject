package com.boardProject.emailVerification.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Setter
public class EmailVerification extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long emailVerificationId;
    @Column(nullable = false)
    private String code;

    // 인증이 완료되면 true로 선언해주며 jpql을 이용하여 인증 완료된 코드는 검색에서 제외
    @Column(nullable = false)
    private Boolean verified=false;

    @OneToOne
    @JoinColumn(name="MEMBER_ID",nullable = false)
    Member member;

    public EmailVerification(String code) {
        this.code = code;
    }

    public void setMember(Member member){
        this.member = member;
        if(member.getEmailVerification() != this) member.setEmailVerification(this);
    }
}
