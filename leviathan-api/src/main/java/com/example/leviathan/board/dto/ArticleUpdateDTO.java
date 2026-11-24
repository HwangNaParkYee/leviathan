package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateDTO {
    private String title;
    private String content;
    private String code;

    public ArticleUpdateDTO(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
