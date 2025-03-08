package com.example.leviathan.domain;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class ArticleLikes {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articleId")
    private Article articleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;
}
