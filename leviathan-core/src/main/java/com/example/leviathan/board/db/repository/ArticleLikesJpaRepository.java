package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.ArticleLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikesJpaRepository extends JpaRepository<ArticleLikes, Long> {

    Optional<ArticleLikes> findByArticleIdAndMemberId(Long articleId, Long memberId);

    // 게시물의 좋아요수 조회
    Long countByArticleId(Long articleId);

}
