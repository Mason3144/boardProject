package com.boardProject.board.repository;

import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes, Long> {
}
