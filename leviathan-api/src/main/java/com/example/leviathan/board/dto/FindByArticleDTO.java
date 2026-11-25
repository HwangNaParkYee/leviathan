package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByArticleDTO {
    private Long articleId;
    private Long problemNumber;
    private Long problemId;
    private String title;
    private String content;
    private String code;
    private String memberName;
    private Long likes;
    private Instant updateDate;

    public FindByArticleDTO(Article article, Long likes) {
        this.articleId = article.getId();
        this.problemNumber = article.getProblemId().getNumber();
        this.problemId = article.getProblemId().getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.code = article.getCode();
        this.memberName = article.getMemberId().getMemberId();
        this.likes = likes;
        this.updateDate = article.getUpdatedAt();
    }
}
