package com.example.leviathan.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Favorite {
    @Id @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "problemId")
    private Problem problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member memberId;

}
