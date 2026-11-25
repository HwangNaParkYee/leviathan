package com.example.leviathan.board.facade;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.service.ArticleLikesService;
import com.example.leviathan.board.service.ArticleService;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArticleLikesFacade {

    private final ArticleLikesService articleLikesService;
    private final ArticleService articleService;
    private final MemberService memberService;

    @Transactional
    public ResponseEntity<String> toggleLike(String loginId, Long articleId) {
        Member member = memberService.findByLoginId(loginId);
        Article article = articleService.getById(articleId);

        if (articleLikesService.toggle(member, article)) {
            return ResponseEntity.ok("좋아요 성공");
        }
        return ResponseEntity.ok("좋아요 취소!!");
    }

    public ResponseEntity<String> findByArticleIdMemberArticleLike(String loginId, Long articleId) {
        Member member = memberService.findByLoginId(loginId);

        if (member == null) {
            return ResponseEntity.ok("사용자 없음");
        }

        Article article = articleService.getById(articleId);

        if (articleLikesService.isLiked(member, article)) {
            return ResponseEntity.ok("좋아요 있음");
        }
        return ResponseEntity.ok("좋아요 없음");
    }
}
