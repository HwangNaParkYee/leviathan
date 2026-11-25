package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Article;

public record SaveArticleResponse(
        Long problemNumber,
        String title,
        String content
) {
    public static SaveArticleResponse from(final Article article) {
        return new SaveArticleResponse(
                article.getProblemId().getNumber(),
                article.getTitle(),
                article.getContent()
        );
    }
}
