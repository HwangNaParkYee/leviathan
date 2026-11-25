package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Comment;

import java.time.Instant;

public record FindByArticleIdCommentResponse(
        Long id,
        Long articleId,
        String memberId,
        String content,
        Instant createDate,
        Instant updateDate,
        long likeCount
) {
    public static FindByArticleIdCommentResponse of(final Comment comment, final long likeCount) {
        return new FindByArticleIdCommentResponse(
                comment.getId(),
                comment.getArticleId().getId(),
                comment.getMemberId().getMemberId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                likeCount
        );
    }
}
