package com.example.leviathan.problem.controller.request;

public record SubmitProblemRequest(
        Long problemId,
        String answer
) {
}
