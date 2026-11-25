package com.example.leviathan.problem.facade.response;

import com.example.leviathan.problem.db.entity.Problem;

public record FindByAllProblemResponse(
        Long problemId,
        Long problemNumber,
        String title,
        Long likes,
        int lv,
        Double correct
) {
    public static FindByAllProblemResponse of(final Problem problem, final Long likes, final Double correct) {
        return new FindByAllProblemResponse(
                problem.getId(),
                problem.getNumber(),
                problem.getTitle(),
                likes,
                problem.getLv(),
                correct
        );
    }
}
