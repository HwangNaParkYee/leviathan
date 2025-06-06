package com.example.leviathan.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class MyProblem {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
    private LocalDateTime createDate;

    public MyProblem(Problem problem, Member member) {
        this.problem = problem;
        this.member = member;
        this.createDate = LocalDateTime.now();
    }
}
