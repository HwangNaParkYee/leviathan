package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FindByAllArticleDTO {
    private Long id;
    private String title;
    private String memberName;
    private LocalDate updateDate;
    private Long likes;
    private Long commentsNumber;

    public FindByAllArticleDTO(Article article, Long likes, Long commentsNumber) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.memberName = article.getMemberId().getMemberId();
        this.updateDate = article.getUpdateDate();
        this.likes = likes;
        this.commentsNumber = commentsNumber;
    }
}
