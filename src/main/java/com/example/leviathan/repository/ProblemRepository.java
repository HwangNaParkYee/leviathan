package com.example.leviathan.repository;

import com.example.leviathan.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Problem findByNumber(Long number);

    // 모든 문제 조회
    List<Problem> findAll();
}
