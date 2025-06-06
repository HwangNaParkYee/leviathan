package com.example.leviathan.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Article {
    @Id @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problemId")
    private Problem problemId;
    private String title;
    private String content;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Article(Problem problemId, String title, String content,String code, Member memberId) {
        this.problemId = problemId;
        this.title = title;
        this.content = content;
        this.code = code;
        this.memberId = memberId;
        this.createDate = LocalDateTime.now();
        this.updateDate = this.createDate;
    }

    public void updateArticle(String title, String content, String code) {
        this.title = title;
        this.content = content;
        this.code = code;
        this.updateDate = LocalDateTime.now();
    }
}
