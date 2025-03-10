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
    private Double correctRate;


    public FindProblemDTO(Problem problem) {
        this.problemId = problem.getId();
        this.problemNumber = Long.valueOf(problem.getNumber());
        this.title = problem.getTitle();
        this.question = problem.getQuestion();
        this.hint = problem.getHint();
        // 즐겨찾기한 로직 아직 미구현 
        this.likes = 0L;
        // 정답률 로직 아직 미구현
        this.correctRate = 0.0;

    }

}
