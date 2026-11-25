package com.example.leviathan.problem.port;

import com.example.leviathan.problem.model.GeneratedProblem;

public interface ProblemGenerator {

    boolean isValidTopic(String topic);

    GeneratedProblem generate(String topic, int level);
}
