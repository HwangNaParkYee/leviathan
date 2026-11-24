package com.example.leviathan.problem.facade.response;

import com.example.leviathan.problem.db.entity.Problem;

public record MyProblemResponse(
        Long problemId,
        Long problemNumber,
        String title
) {
    public static MyProblemResponse from(final Problem problem) {
        return new MyProblemResponse(problem.getId(), problem.getNumber(), problem.getTitle());
    }
}
