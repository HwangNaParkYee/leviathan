package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.ArticleLikes;
import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.ArticleLikesDTO;
import com.example.leviathan.repository.ArticleLikesRepository;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.security.JwtUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ArticleLikesService {
    private final ArticleRepository articleRepository;
    private final ArticleLikesRepository articleLikesRepository;
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;

    @Transactional
    public ResponseEntity<ArticleLikesDTO> toggleLike(Long articleId, String token) {
        // 사용자 찾기
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberId(token));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));


        //좋아요 눌렀는지 확인
        boolean liked = articleLikesRepository.existsByArticleIdAndMemberId(article, member);

        if(liked) {
            articleLikesRepository.deleteByArticleIdAndMemberId(article, member);
            return ResponseEntity.ok(new ArticleLikesDTO(article));
        } else {
            ArticleLikes newLike = new ArticleLikes(article, member);
            articleLikesRepository.save(newLike);
            return ResponseEntity.ok(new ArticleLikesDTO(article));
        }
    }

    @Transactional
    public Long countLikes(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        long likesCount = articleLikesRepository.countByArticleId(article);

        return likesCount;
    }
}
