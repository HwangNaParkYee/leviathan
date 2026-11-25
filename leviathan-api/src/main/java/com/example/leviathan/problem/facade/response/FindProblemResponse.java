package com.example.leviathan.problem.facade.response;

import com.example.leviathan.problem.db.entity.Problem;

public record FindProblemResponse(
        Long problemId,
        Long problemNumber,
        String title,
        String question,
        String hint,
        Long likes,
        int lv,
        Double correctRate
) {
    public static FindProblemResponse of(final Problem problem, final Long likes, final Double correctRate) {
        return new FindProblemResponse(
                problem.getId(),
                problem.getNumber(),
                problem.getTitle(),
                problem.getQuestion(),
                problem.getHint(),
                likes,
                problem.getLv(),
                correctRate
        );
    }
}
