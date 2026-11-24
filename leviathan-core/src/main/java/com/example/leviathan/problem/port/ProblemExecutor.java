package com.example.leviathan.problem.port;

import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.model.GeneratedProblem;

public interface ProblemExecutor {

    // AI가 생성한 문제의 실행 환경(이미지) 준비
    void prepare(Problem problem, GeneratedProblem generated);

    // 사용자 명령어 실행 결과 확인용
    ExecutionResult run(Problem problem, String command);

    // 사용자 명령어 채점용
    ExecutionResult judge(Problem problem, String command);
}
