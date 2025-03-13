package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLikesDTO {
    private Long articleId;

    public ArticleLikesDTO(Article article) {
        this.articleId = article.getId();
    }
}
