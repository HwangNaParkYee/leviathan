package com.example.leviathan.problem.controller.request;

public record UpdateProblemRequest(
        Long problemId,
        Long problemNumber,
        String title,
        String question,
        String hint,
        int lv
) {
}
