package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class saveCommentDTO{
    private String token;
    private Long articleId;
    private String content;
}