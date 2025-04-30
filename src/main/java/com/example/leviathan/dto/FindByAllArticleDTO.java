package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FindByAllArticleDTO {
    private Long id;
    private String title;
    private String memberId;
    private Long problemNumber;
    private Long problemId;
    private LocalDate updateDate;
    private Long likes;
    private Long commentsNumber;

    public FindByAllArticleDTO(Article article, Long likes, Long commentsNumber) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.memberId = article.getMemberId().getMemberId();
        this.problemId = article.getProblemId().getId();
        this.problemNumber = article.getProblemId().getNumber();
        this.updateDate = article.getUpdateDate();
        this.likes = likes;
        this.commentsNumber = commentsNumber;
    }
}
