package com.boardProject.board.entity;

import com.boardProject.audit.Auditable;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Photos extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long photoId;
    @ManyToOne
    @JoinColumn(name = "POST_ID",nullable = false)
    private Posts posts;
    @Column(nullable = false)
    private String filePath;
    public Photos(String filePath) {
        this.filePath = filePath;
    }
    public void setPosts(Posts post){
        this.posts = post;
    }
}
