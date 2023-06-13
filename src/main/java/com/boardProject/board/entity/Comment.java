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
public class Comment extends Auditable {
    // jpql count 사용해보기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    @Enumerated(value = EnumType.STRING)
    private CommentStatus commentStatus = CommentStatus.COMMENT_ALIVE;
    @Column(columnDefinition = "TEXT",nullable = false) //  varchar 보다 사이즈가 큰 텍스트 정의
    private String content;
    @ManyToOne
    @JoinColumn(name = "POST_ID",nullable = false)
    private Posts posts;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID",nullable = false)
    private Member member;

    public enum CommentStatus{
        COMMENT_ALIVE("Comment alive"),
        COMMENT_DELETED("Comment deleted");
        @Getter
        private String status;

        CommentStatus(String status) {
            this.status = status;
        }
    }
}
