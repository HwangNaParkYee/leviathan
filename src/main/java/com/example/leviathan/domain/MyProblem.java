package com.example.leviathan.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class MyProblem {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Problem problemId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member memberId;
    private LocalDate createDate;

    public MyProblem(Problem problem, Member member) {
        this.problemId = problem;
        this.memberId = member;
        this.createDate = LocalDate.now();
    }
}
