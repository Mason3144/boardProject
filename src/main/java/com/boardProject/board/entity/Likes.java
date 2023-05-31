package com.boardProject.board.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Likes extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likesId;
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Posts posts;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
