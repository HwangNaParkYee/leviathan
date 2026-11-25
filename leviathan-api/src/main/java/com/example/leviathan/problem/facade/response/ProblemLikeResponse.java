package com.example.leviathan.problem.facade.response;

import com.example.leviathan.problem.db.entity.Problem;

public record ProblemLikeResponse(
        Long problemId,
        Long problemNumber
) {
    public static ProblemLikeResponse from(final Problem problem) {
        return new ProblemLikeResponse(problem.getId(), problem.getNumber());
    }
}
