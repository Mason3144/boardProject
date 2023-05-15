package com.boardProject.board.entity;

import com.boardProject.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long likesId;
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public void setPost(Post post){
        this.post = post;
    }
    public void setMember(Member member){
        this.member = member;
    }
}
