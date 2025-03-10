package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleUpdateDTO {
    private Long id;
    private String title;
    private String content;

    public ArticleUpdateDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
    }
}
