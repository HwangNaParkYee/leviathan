package com.example.leviathan.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class FindByArticleIdCommentDTO {
    private Long id;
    private Long articleId;
    private String memberId;
    private String content;
    private Instant createDate;
    private Instant updateDate;
    private long likeCount;
}
