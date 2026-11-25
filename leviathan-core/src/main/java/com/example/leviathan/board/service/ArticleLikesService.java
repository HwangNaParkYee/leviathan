package com.example.leviathan.board.service;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.db.entity.ArticleLikes;
import com.example.leviathan.board.db.repository.ArticleLikesRepository;
import com.example.leviathan.member.db.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikesService {

    private final ArticleLikesRepository articleLikesRepository;

    @Transactional
    public boolean toggle(Member member, Article article) {
        Optional<ArticleLikes> articleLikes = articleLikesRepository.findByArticleIdAndMemberId(article.getId(), member.getId());

        if (articleLikes.isPresent()) {
            articleLikesRepository.delete(articleLikes.get());
            return false;
        }
        articleLikesRepository.save(ArticleLikes.create(article, member));
        return true;
    }

    public Long count(Article article) {
        return articleLikesRepository.countByArticleId(article.getId());
    }

    public boolean isLiked(Member member, Article article) {
        return articleLikesRepository.findByArticleIdAndMemberId(article.getId(), member.getId()).isPresent();
    }
}
