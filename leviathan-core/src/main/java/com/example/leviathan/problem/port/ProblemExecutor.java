package com.example.leviathan.problem.port;

import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.model.GeneratedProblem;

public interface ProblemExecutor {

    void prepare(Problem problem, GeneratedProblem generated);

    ExecutionResult run(Problem problem, String command);

    ExecutionResult judge(Problem problem, String command);
}
