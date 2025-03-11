package com.example.leviathan.repository;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.ArticleLikes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikesRepository extends JpaRepository<ArticleLikes, Long> {
    // 사용자가 좋아요를 눌렀을때 눌렀는지 확인
    boolean existsByArticleIdAndMemberId(Article articleId, Member memberId);

    // 게시물의 좋아요수 조회
    long countByArticleId(Article articleId);

    // 좋아요 다시 눌렀을때 삭제
    void deleteByArticleIdAndMemberId(Article articleId, Member memberId);
}
