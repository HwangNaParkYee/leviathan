package com.example.leviathan.review.db.entity;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    public static final Long RIGHT = 1L;
    public static final Long WRONG = 0L;

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

    public Review(Problem problemId, Member memberId, String answer, Long isRight) {
        this.memberId = memberId;
        this.problemId = problemId;
        this.answer = answer;
        this.createDate = LocalDateTime.now();
        this.isRight = isRight;
    }

    public static Review submit(Problem problem, Member member, String answer, boolean correct) {
        return new Review(problem, member, answer, correct ? RIGHT : WRONG);
    }

    public boolean isCorrect() {
        return RIGHT.equals(isRight);
    }

    public static double correctRate(long totalSubmissions, long correctSubmissions) {
        if (totalSubmissions == 0) {
            return 0.0;
        }
        return ((double) correctSubmissions / totalSubmissions) * 100;
    }
}
