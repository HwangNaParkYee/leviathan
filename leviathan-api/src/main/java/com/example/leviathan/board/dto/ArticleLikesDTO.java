package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Article;
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
