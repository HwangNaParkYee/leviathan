package com.example.leviathan.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FindByArticleIdCommentDTO {
    private Long id;
    private Long articleId;
    private String memberId;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private long likeCount;
}
