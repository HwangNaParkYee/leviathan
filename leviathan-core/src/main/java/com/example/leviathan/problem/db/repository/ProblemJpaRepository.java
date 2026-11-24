package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemJpaRepository extends JpaRepository<Problem, Long> {
    Problem findByNumber(Long number);

    // 모든 문제 조회
    List<Problem> findAll();

    @Query("SELECT p.number FROM Problem p")
    List<Long> findAllProblemNumbers();
}
