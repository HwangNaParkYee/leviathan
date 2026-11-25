package com.example.leviathan.problem.controller.request;

public record UserPromptRequest(
        String prompt,
        int level
) {
}
