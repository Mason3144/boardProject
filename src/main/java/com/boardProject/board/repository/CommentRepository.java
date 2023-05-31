package com.boardProject.board.repository;

import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
