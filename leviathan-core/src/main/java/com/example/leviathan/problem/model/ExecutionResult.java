package com.example.leviathan.problem.model;

public record ExecutionResult(int exitCode, String output, String errorOutput) {

    private static final int CORRECT_EXIT_CODE = 3;

    public boolean isCorrect() {
        return exitCode == CORRECT_EXIT_CODE;
    }

    public boolean isSuccess() {
        return exitCode == 0;
    }
}
