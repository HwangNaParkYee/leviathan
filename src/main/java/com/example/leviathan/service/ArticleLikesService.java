package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.ArticleLikes;
import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.ArticleLikesDTO;
import com.example.leviathan.repository.ArticleLikesRepository;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleLikesService {
    private final ArticleRepository articleRepository;
    private final ArticleLikesRepository articleLikesRepository;
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<String> toggleLike(HttpServletRequest header, Long articleId) {

        String token = jwtUtility.extractAccessTokenFromCookie(header);
        // 사용자 찾기
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));


        // 해당 글에 사용자가 추천을 이미 했는지 확인
        Optional<ArticleLikes> articleLikes = articleLikesRepository.findByArticleIdAndMemberId(article.getId(), member.getId());


        if(articleLikes.isPresent()) {
            articleLikesRepository.delete(articleLikes.get()); 
            return ResponseEntity.ok("좋아요 취소!!");
        } else {
            ArticleLikes newLike = new ArticleLikes(article, member);
            articleLikesRepository.save(newLike);
            return ResponseEntity.ok("좋아요 성공");
        }
    }

    @Transactional
    public Long countLikes(Article article) {
        Long likesCount = articleLikesRepository.countByArticleId(article.getId());

        return likesCount;
    }

    public ResponseEntity<String> findByArticleIdMemberArticleLike(HttpServletRequest header, Long articleId) {
        String token = jwtUtility.extractAccessTokenFromCookie(header);
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));

        if (member == null){
            return ResponseEntity.ok("사용자 없음");
        }

        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        Optional<ArticleLikes> articleLikes = articleLikesRepository.findByArticleIdAndMemberId(article.getId(), member.getId());


        if(articleLikes.isPresent()) {
            return ResponseEntity.ok("좋아요 있음");
        } else {
            ArticleLikes newLike = new ArticleLikes(article, member);
            return ResponseEntity.ok("좋아요 없음");
        }
    }
}
