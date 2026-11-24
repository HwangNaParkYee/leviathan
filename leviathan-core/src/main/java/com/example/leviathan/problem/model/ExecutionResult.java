package com.example.leviathan.problem.model;

// 문제 컨테이너 실행 결과
public record ExecutionResult(int exitCode, String output, String errorOutput) {

    // run.sh는 정답이면 exit code 3으로 종료한다
    private static final int CORRECT_EXIT_CODE = 3;

    public boolean isCorrect() {
        return exitCode == CORRECT_EXIT_CODE;
    }

    public boolean isSuccess() {
        return exitCode == 0;
    }
}
