package com.example.leviathan.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Problem {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true)  // 유니크 제약 조건 추가
    private Long number;
    private String title;
    private String question;
    private String hint;

    public Problem(Long number, String title, String question, String hint) {
        this.number = number;
        this.title = title;
        this.question = question;
        this.hint = hint;
    }


}
