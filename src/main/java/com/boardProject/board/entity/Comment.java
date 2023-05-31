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
public class Comment extends Auditable {
    // jpql count 사용해보기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long commentId;
    @Enumerated(value = EnumType.STRING)
    private CommentStatus status;

    // member
    // post

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
