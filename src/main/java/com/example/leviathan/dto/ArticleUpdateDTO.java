package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateDTO {
    private String title;
    private String content;

    public ArticleUpdateDTO(Article article) {
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
