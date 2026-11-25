package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Comment;
import lombok.Getter;

import java.time.Instant;

@Getter
public class CommentDTO {
    private final Long id;
    private final String content;
    private final Long articleId;
    private final String memberId;
    private final Instant createdAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.articleId = comment.getArticleId().getId();
        this.memberId = comment.getMemberId().getMemberId();
        this.createdAt = comment.getCreatedAt();
    }
}