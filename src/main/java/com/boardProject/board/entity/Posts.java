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
public class Posts extends Auditable {
    public Posts(long postId) {
        this.postId = postId;
    }

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
    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new LinkedList<>();
    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new LinkedList<>();
    @OneToMany(mappedBy = "posts", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Photos> photos = new LinkedList<>();
    @Column(columnDefinition = "TEXT") //  varchar 보다 사이즈가 큰 텍스트 정의
    private String content;

    public void setPhotos(Photos photo){
        this.photos.add(photo);
        if(photo.getPosts()!=this) photo.setPosts(this);
    }
    public void setComments(Comment comment){
        this.comments.add(comment);
        if(comment.getPosts()!=this) comment.setPosts(this);
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
