package com.example.leviathan.problem.db.entity;

import com.example.leviathan.member.db.entity.Member;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
