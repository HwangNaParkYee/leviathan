package com.example.leviathan.repository;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.ArticleLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Long> {

    Optional<ArticleLikes> findByArticleIdAndMemberId(Long articleId, Long memberId);

    // 게시물의 좋아요수 조회
    Long countByArticleId(Long articleId);

}
