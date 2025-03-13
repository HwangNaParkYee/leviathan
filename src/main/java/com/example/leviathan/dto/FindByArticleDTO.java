package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByArticleDTO {
    private Long problemNumber;
    private String title;
    private String content;
    private String memberName;
    private Long likes;
    private LocalDate updateDate;

    public FindByArticleDTO(Article article, Long likes) {
        this.problemNumber = article.getProblemId().getNumber();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.memberName = article.getMemberId().getMemberId();
        this.likes = likes;
        this.updateDate = article.getUpdateDate();
    }
}
