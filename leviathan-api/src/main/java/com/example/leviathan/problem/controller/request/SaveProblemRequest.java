package com.example.leviathan.problem.controller.request;

public record SaveProblemRequest(
        Long number,
        String title,
        String question,
        String hint,
        int lv
) {
}
