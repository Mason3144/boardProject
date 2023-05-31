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
    private CommentStatus status;
    @Column(columnDefinition = "TEXT") //  varchar 보다 사이즈가 큰 텍스트 정의
    private String content;
    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Posts posts;
    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    public enum CommentStatus{
        COMMENT_ACTIVE("Comment alive"),
        COMMENT_DELETED("Comment deleted");
        @Getter
        private String status;

        CommentStatus(String status) {
            this.status = status;
        }
    }
}
