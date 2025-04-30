package com.example.leviathan.repository;

import com.example.leviathan.domain.Article;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Problem findByNumber(Long number);

    // 모든 문제 조회
    List<Problem> findAll();

    @Query("SELECT p.number FROM Problem p")
    List<Long> findAllProblemNumbers();
}
