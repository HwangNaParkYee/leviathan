package com.example.leviathan.problem.port;

import com.example.leviathan.problem.model.GeneratedProblem;

public interface ProblemGenerator {

    // 리눅스 실습 문제로 적절한 요청인지 판단
    boolean isValidTopic(String topic);

    GeneratedProblem generate(String topic, int level);
}
