package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FindByAllArticleDTO {
    private String title;
    private String memberName;
    private LocalDate updateDate;
    private Long likes;

    public FindByAllArticleDTO(Article article) {
        this.title = article.getTitle();
        this.memberName = article.getMemberId().getMemberId();
        this.updateDate = article.getUpdateDate();
        // repository에서 좋아요 개수 로직 추가
    }
}
