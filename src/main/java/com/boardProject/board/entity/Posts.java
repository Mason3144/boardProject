package com.boardProject.board.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Posts extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long postId;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int views;
    @Enumerated(value = EnumType.STRING)
    private PostStatus postStatus;
    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private Member member;
    @OneToMany(mappedBy = "posts")
    private List<Likes> likes = new LinkedList<>();
    @OneToOne(mappedBy = "posts",cascade = CascadeType.ALL)
    private Content content;

    public void setContent(Content content){
        this.content = content;
        if(content.getPosts() != this) content.setPosts(this);
    }
    public void setLikes(Likes likes){
        this.likes.add(likes);
        if(likes.getPosts()!=this) likes.setPosts(this);
    }
    // 상태 추가
    public void setMember(Member member){
        this.member = member;
    }
    public void setViews(){this.views++;}


    public enum PostStatus{
        POST_PUBLIC("public post"),
        POST_PRIVATE("private post"),
        POST_DELETED("deleted post");

        private String status;

        PostStatus(String status) {
            this.status = status;
        }

        @JsonValue
        public String getStatus() {
            return status;
        }

    }
}
