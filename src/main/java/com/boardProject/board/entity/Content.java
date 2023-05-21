package com.boardProject.board.entity;

import com.boardProject.audit.Auditable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Content extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long contentId;
    @Column(columnDefinition = "TEXT") //  varchar 보다 사이즈가 큰 텍스트 정의
    private String content;
    @OneToOne
    @JoinColumn(name="POST_ID")
    private Post post;
    public void setPost(Post post){
        this.post = post;
        if(post.getContent() != this) post.setContent(this);
    }

}
