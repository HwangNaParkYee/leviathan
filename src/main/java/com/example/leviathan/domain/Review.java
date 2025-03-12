package com.example.leviathan.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

public class Review {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problemId")
    private Problem problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;

    private String answer;
    private LocalDate createDate;
    private Boolean isRight;
}