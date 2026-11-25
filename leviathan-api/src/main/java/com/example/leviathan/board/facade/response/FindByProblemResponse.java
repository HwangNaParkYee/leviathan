package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Article;

import java.time.Instant;

public record FindByProblemResponse(
        Long id,
        Long problemId,
        Long problemNumber,
        String title,
        String memberId,
        Instant updateDate,
        Long likes,
        Long commentNumbers
) {
    public static FindByProblemResponse of(final Article article, final Long likes, final Long commentNumbers) {
        return new FindByProblemResponse(
                article.getId(),
                article.getProblemId().getId(),
                article.getProblemId().getNumber(),
                article.getTitle(),
                article.getMemberId().getMemberId(),
                article.getUpdatedAt(),
                likes,
                commentNumbers
        );
    }
}
