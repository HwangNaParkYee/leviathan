package com.example.leviathan.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Article {
    @Id
    private Long id;
    private Long problemNumber;
    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;
    private LocalDate createDate;
    private LocalDate updateDate;
}
