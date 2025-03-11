package com.example.leviathan.service;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.repository.ArticleLikesRepository;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.MemberRepository;
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

//    @Transactional
//    public ResponseEntity<String> toggleLike(Long articleId, String token) {
//        // 토큰으로 멤버 생성하기
//
//        Article article = articleRepository.findById(articleId)
//                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));
//
//        // 멤버 찾기
//
//        //좋아요 눌렀는지 확인
//        boolean liked = articleLikesRepository.existsByArticleIdAndMemberId(article,)
//    }

    @Transactional
    public ResponseEntity<Long> countLikes(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물을 찾을 수 없습니다."));

        long likesCount = articleLikesRepository.countByArticleId(article);

        return ResponseEntity.ok(likesCount);
    }
}
