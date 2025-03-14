package com.example.leviathan.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;

    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public Comment(Article article, Member member, String content, LocalDateTime createDate, LocalDateTime updateDate) {
        this.articleId = article;
        this.memberId = member;
        this.content = content;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
