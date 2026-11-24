package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Article;

import java.time.Instant;

public record FindByArticleResponse(
        Long articleId,
        Long problemNumber,
        Long problemId,
        String title,
        String content,
        String code,
        String memberName,
        Long likes,
        Instant updateDate
) {
    public static FindByArticleResponse of(final Article article, final Long likes) {
        return new FindByArticleResponse(
                article.getId(),
                article.getProblemId().getNumber(),
                article.getProblemId().getId(),
                article.getTitle(),
                article.getContent(),
                article.getCode(),
                article.getMemberId().getMemberId(),
                likes,
                article.getUpdatedAt()
        );
    }
}
