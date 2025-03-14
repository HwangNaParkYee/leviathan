package com.example.leviathan.dto;

import com.example.leviathan.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentDTO {
    private final Long id;
    private final String content;
    private final Long articleId;
    private final String memberId;
    private final LocalDateTime createdAt;

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.articleId = comment.getArticleId().getId();
        this.memberId = comment.getMemberId().getMemberId();
        this.createdAt = comment.getCreateDate();
    }
}