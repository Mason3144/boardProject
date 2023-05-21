package com.boardProject.board.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Post extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;
    @Column(nullable = false)
    private PostStatus state;
    @Enumerated(value = EnumType.STRING)
    private PostStatus postStatus;
    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "post")
    private List<Likes> likes = new LinkedList<>();
    @OneToOne(mappedBy = "Post")
    private Content content;

    public void setContent(Content content){
        this.content = content;
        if(content.getPost() != this) content.setPost(this);
    }
    public void setLikes(Likes likes){
        this.likes.add(likes);
        if(likes.getPost()!=this) likes.setPost(this);
    }
    // 상태 추가
    public void setMember(Member member){
        this.member = member;
    }
    public enum PostStatus{
        POST_PUBLIC("공개 포스트"),
        POST_PRIVATE("비밀 포스트"),
        POST_DELETED("삭제된 포스트");

        @Getter
        private String status;

        PostStatus(String status) {
            this.status = status;
        }
    }
}
