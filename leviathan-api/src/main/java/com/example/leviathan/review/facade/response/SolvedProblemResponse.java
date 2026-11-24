package com.example.leviathan.review.facade.response;

import com.example.leviathan.review.model.SolvedProblem;

public record SolvedProblemResponse(
        Long problemId,
        Long problemNumber
) {
    public static SolvedProblemResponse from(final SolvedProblem problem) {
        return new SolvedProblemResponse(problem.problemId(), problem.problemNumber());
    }
}
