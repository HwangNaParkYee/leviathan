package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.Problem;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository {

    Problem save(Problem problem);

    Optional<Problem> findById(Long id);

    List<Problem> findAll();

    Problem findByNumber(Long number);

    List<Long> findAllProblemNumbers();
}
