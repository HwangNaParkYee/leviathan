package com.example.leviathan.problem.dto;

import com.example.leviathan.problem.db.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class  MyProblemDTO{

    private Long problemId;
    private Long problemNumber;
    private String title;

    public MyProblemDTO(Problem problem) {
        this.problemId = problem.getId();
        this.problemNumber = problem.getNumber();
        this.title = problem.getTitle();

    }
}
