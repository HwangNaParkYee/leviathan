package com.example.leviathan.board.facade;

import com.example.leviathan.board.db.entity.Article;
import com.example.leviathan.board.service.ArticleLikesService;
import com.example.leviathan.board.service.ArticleService;
import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;
import com.example.leviathan.member.service.MemberService;
import lombok.RequiredArgsConstructor;
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
    public void toggleLike(String loginId, Long articleId) {
        Member member = getMember(loginId);
        Article article = articleService.getById(articleId);

        articleLikesService.toggle(member, article);
    }

    public boolean isLiked(String loginId, Long articleId) {
        Member member = getMember(loginId);
        Article article = articleService.getById(articleId);

        return articleLikesService.isLiked(member, article);
    }

    private Member getMember(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return member;
    }
}
