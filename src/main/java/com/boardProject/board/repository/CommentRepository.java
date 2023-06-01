package com.boardProject.board.repository;

import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.commentId = :commentId and not (c.commentStatus = 'COMMENT_DELETED')")
    Optional<Comment> findById(Long commentId);
}
