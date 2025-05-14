package com.example.leviathan.dto;

import com.example.leviathan.domain.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindProblemDTO {

    private Long problemId;
    private Long problemNumber;
    private String title;
    private String question;
    private String hint;
    private Long likes;
    private int lv;
    private Double correctRate;


    public FindProblemDTO(Problem problem) {
        this.problemId = problem.getId();
        this.problemNumber = problem.getNumber();
        this.title = problem.getTitle();
        this.question = problem.getQuestion();
        this.hint = problem.getHint();
        // 즐겨찾기한 로직 아직 미구현 
        this.likes = 0L;
        this.correctRate = 0.0;

    }

    public FindProblemDTO(Problem problem,Long likes, Double correctRate) {
        this.problemId = problem.getId();
        this.problemNumber = problem.getNumber();
        this.title = problem.getTitle();
        this.question = problem.getQuestion();
        this.hint = problem.getHint();
        // 즐겨찾기한 로직 아직 미구현
        this.likes = likes;
        this.lv = problem.getLv();
        this.correctRate = correctRate;

    }

}
