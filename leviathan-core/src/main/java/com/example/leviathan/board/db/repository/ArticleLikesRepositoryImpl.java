package com.example.leviathan.board.db.repository;

import com.example.leviathan.board.db.entity.ArticleLikes;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleLikesRepositoryImpl implements ArticleLikesRepository {

    private final ArticleLikesJpaRepository jpaRepository;

    @Override
    public ArticleLikes save(ArticleLikes articleLikes) {
        return jpaRepository.save(articleLikes);
    }

    @Override
    public void delete(ArticleLikes articleLikes) {
        jpaRepository.delete(articleLikes);
    }

    @Override
    public Optional<ArticleLikes> findByArticleIdAndMemberId(Long articleId, Long memberId) {
        return jpaRepository.findByArticleIdAndMemberId(articleId, memberId);
    }

    @Override
    public Long countByArticleId(Long articleId) {
        return jpaRepository.countByArticleId(articleId);
    }
}
