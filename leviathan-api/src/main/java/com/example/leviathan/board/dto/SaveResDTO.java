package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveResDTO {
    private Long problemNumber;
    private String title;
    private String content;

    public SaveResDTO(Article article) {
        this.problemNumber = article.getProblemId().getNumber();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
