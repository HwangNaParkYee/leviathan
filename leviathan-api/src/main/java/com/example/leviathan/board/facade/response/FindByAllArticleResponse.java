package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Article;

import java.time.Instant;

public record FindByAllArticleResponse(
        Long id,
        String title,
        String memberId,
        Long problemNumber,
        Long problemId,
        Instant updateDate,
        Long likes,
        Long commentsNumber
) {
    public static FindByAllArticleResponse of(final Article article, final Long likes, final Long commentsNumber) {
        return new FindByAllArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getMemberId().getMemberId(),
                article.getProblemId().getNumber(),
                article.getProblemId().getId(),
                article.getUpdatedAt(),
                likes,
                commentsNumber
        );
    }
}
