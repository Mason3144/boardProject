package com.boardProject.board.repository;

import com.boardProject.board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Post, Long> {
    // 게시판을 위한 게시글들
    // 하나의 게시글, 라이크 수 카운팅과 isLiked
    //검색 - 내글 보기, 특정글보기, 특정멤버의 글보기
    // views 카운팅
    // 라이크 on off
}
