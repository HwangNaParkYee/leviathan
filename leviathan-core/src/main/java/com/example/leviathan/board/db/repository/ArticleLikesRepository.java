package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.ArticleLikes;

import java.util.Optional;

public interface ArticleLikesRepository {

    ArticleLikes save(ArticleLikes articleLikes);

    void delete(ArticleLikes articleLikes);

    Optional<ArticleLikes> findByArticleIdAndMemberId(Long articleId, Long memberId);

    Long countByArticleId(Long articleId);
}
