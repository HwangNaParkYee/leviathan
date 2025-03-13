package com.example.leviathan.dto;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindByProblemDTO {
    private Long problemId;
    private String title;
    private String memberId;
    private LocalDate updateDate;
    private Long commentNumbers;
    private Long likes;

    public FindByProblemDTO(Article article, Long likes) {
        this.problemId = article.getProblemId().getId();
        this.title = article.getTitle();
        this.memberId = article.getMemberId().getMemberId();
        this.updateDate = article.getUpdateDate();
        this.likes = likes;
        // 코멘트 개수 로직 sql 추가
    }
}
