package com.boardProject.board.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.member.entity.Member;
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

    public void setPosts(Posts posts){
        this.posts = posts;
    }
    public void setMember(Member member){
        this.member = member;
    }
}
