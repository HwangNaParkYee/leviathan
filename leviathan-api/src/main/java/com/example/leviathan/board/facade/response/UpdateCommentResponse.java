package com.example.leviathan.board.facade.response;

import com.example.leviathan.board.db.entity.Comment;

import java.time.Instant;

public record UpdateCommentResponse(
        Long id,
        Long articleId,
        String content,
        String memberId,
        Instant updatedAt
) {
    public static UpdateCommentResponse from(final Comment comment) {
        return new UpdateCommentResponse(
                comment.getId(),
                comment.getArticleId().getId(),
                comment.getContent(),
                comment.getMemberId().getMemberId(),
                comment.getUpdatedAt()
        );
    }
}
