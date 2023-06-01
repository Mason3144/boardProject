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
    // 게시판을 위한 게시글들
    // 하나의 게시글, 라이크 수 카운팅과 isLiked
    //검색 - 내글 보기, 특정글보기, 특정멤버의 글보기
    // views 카운팅
    // 라이크 on off

    @Query("select p from Posts p where p.postId = :postId and not (p.postStatus = 'POST_DELETED')")
    Optional<Posts> findById(Long postId);

    Page<Posts> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
}
