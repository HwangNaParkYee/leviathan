package com.example.leviathan.problem.db.entity;

import com.example.leviathan.problem.model.GeneratedProblem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Problem {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)  // 유니크 제약 조건 추가
    private Long number;
    private String title;
    @Column(name = "question", columnDefinition = "TEXT")
    private String question;
    private String hint;
    private int lv;

    public Problem(Long number, String title, String question, String hint, int lv) {
        this.number = number;
        this.title = title;
        this.question = question;
        this.hint = hint;
        this.lv = lv;
    }

    public static Problem fromGenerated(GeneratedProblem generated) {
        return new Problem(null, generated.getTitle(), generated.getQuestion(), generated.getHint(), generated.getLevel());
    }

    // id를 number에 복사
    public void assignNumberFromId() {
        this.number = this.id;
    }

    // 값이 null이 아니면 필드 업데이트
    public void update(Long number, String title, String question, String hint, int lv) {
        if (number != null) {
            this.number = number;
        }
        if (title != null) {
            this.title = title;
        }
        if (question != null) {
            this.question = question;
        }
        if (hint != null) {
            this.hint = hint;
        }
        this.lv = lv;
    }
}
