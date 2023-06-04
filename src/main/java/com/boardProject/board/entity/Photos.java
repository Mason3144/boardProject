package com.boardProject.board.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Setter
public class Photos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long photoId;
    @ManyToOne
    @JoinColumn(name = "POST_ID")
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
