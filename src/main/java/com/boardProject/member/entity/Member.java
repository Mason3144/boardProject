

package com.boardProject.member.entity;

import com.boardProject.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    @Column
    private String email;
    @Column
    private String name;
    @Column
    private String password;
    @Enumerated
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;
    @Enumerated
    private MemberAuthority memberAuthority = MemberAuthority.MEMBER;


    public enum MemberStatus{
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("활동 중지"),
        MEMBER_QUIT("활동 정지");
        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }
    public enum MemberAuthority{
        MEMBER("Member"),
        MANAGER("Manager");
        @Getter
        private String authority;

        MemberAuthority(String authority) {
            this.authority = authority;
        }
    }

}
