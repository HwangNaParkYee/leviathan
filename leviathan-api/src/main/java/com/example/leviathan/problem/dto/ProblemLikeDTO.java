package com.example.leviathan.problem.dto;

import com.example.leviathan.problem.db.entity.Problem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemLikeDTO {
    private Long problemId;
    private Long problemNumber;

    public ProblemLikeDTO(Problem problem) {
        this.problemId = problem.getId();
        this.problemNumber = problem.getNumber();
    }
}