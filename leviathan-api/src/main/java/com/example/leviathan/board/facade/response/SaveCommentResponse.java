package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Comment;

import java.time.Instant;

public record SaveCommentResponse(
        Long id,
        String content,
        Long articleId,
        String memberId,
        Instant createdAt
) {
    public static SaveCommentResponse from(final Comment comment) {
        return new SaveCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getArticleId().getId(),
                comment.getMemberId().getMemberId(),
                comment.getCreatedAt()
        );
    }
}
