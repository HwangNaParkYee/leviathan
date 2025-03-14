package com.example.leviathan.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime createDate;
    private Long isRight;


    public Review(Problem problemId,Member memberId, String answer, Long isRight) {
        this.memberId = memberId;
        this.problemId = problemId;
        this.answer = answer;
        this.createDate = LocalDateTime.now();
        this.isRight = isRight;
    }

}