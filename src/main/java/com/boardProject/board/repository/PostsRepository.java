package com.boardProject.board.repository;

import com.boardProject.board.entity.Posts;
import com.boardProject.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostsRepository extends JpaRepository<Posts, Long> {
    @Query("select p from Posts p join fetch p.member m join fetch m.emailVerification where p.postId = :postId and not (p.postStatus = 'POST_DELETED')")
    Optional<Posts> findById(Long postId);

    Page<Posts> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
