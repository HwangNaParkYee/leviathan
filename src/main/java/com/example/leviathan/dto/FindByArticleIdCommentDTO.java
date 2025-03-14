package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class FindByArticleIdCommentDTO {
    private Long id;
    private Long articleId;
    private Long memberId;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
