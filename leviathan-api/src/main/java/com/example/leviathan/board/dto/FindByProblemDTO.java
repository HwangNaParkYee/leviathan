package com.example.leviathan.board.dto;

import com.example.leviathan.board.db.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByProblemDTO {
    private Long id;
    private Long problemId;
    private Long problemNumber;
    private String title;
    private String memberId;
    private Instant updateDate;
    private Long likes;
    private Long commentNumbers;

    public FindByProblemDTO(Article article, Long likes, Long commentNumbers) {
        this.id = article.getId();
        this.problemNumber = article.getProblemId().getNumber();
        this.problemId = article.getProblemId().getId();
        this.title = article.getTitle();
        this.memberId = article.getMemberId().getMemberId();
        this.updateDate = article.getUpdatedAt();
        this.likes = likes;
        this.commentNumbers = commentNumbers;
    }
}
