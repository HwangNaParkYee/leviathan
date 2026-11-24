package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Comment;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentResponseDTO {
    private Long id;
    private Long articleId;
    private String content;
    private String memberId;
    private Instant updatedAt;

    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.articleId = comment.getArticleId().getId();
        this.content = comment.getContent();
        this.memberId = comment.getMemberId().getMemberId(); // Member 엔티티에 `getName()`이 있다고 가정
        this.updatedAt = comment.getUpdatedAt();
    }
}