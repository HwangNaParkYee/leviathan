package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveArticleDTO {
    private Long number;
    private String title;
    private String question;
    private String hint;

    public SaveArticleDTO(Article article) {
        this.number = article.getProblemId().getNumber();
        this.title = article.getTitle();
        this.question = article.getProblemId().getQuestion();
        this.hint = article.getProblemId().getHint();
    }
}
