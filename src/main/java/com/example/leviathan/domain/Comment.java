package com.example.leviathan.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class Comment {
    @Id @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;

    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
