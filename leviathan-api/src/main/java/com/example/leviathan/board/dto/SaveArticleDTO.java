package com.example.leviathan.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveArticleDTO {
    private Long problemId;
    private String title;
    private String content;
    private String code;

}
