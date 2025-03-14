package com.example.leviathan.repository;

import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r.memberId.id, r.isRight " +
            "FROM Review r " +
            "WHERE r.problemId.id = :problemId " +
            "ORDER BY r.memberId.id, r.createDate ASC")
    List<Object[]> findFirstReviewByProblem(@Param("problemId") Long problemId);


    List<Review> findByProblemId(Problem problemId);

    List<Review> findByProblemIdAndMemberId(Problem problemId, Member memberId);

    List<Review> findByMemberId(Member memberId);

    // 특정 문제(problemId)의 전체 제출 횟수
    long countByProblemId(Problem problemId);

    // 특정 문제(problemId)의 맞춘 횟수 (isRight = 1)
    long countByProblemIdAndIsRight(Problem problemId, Long isRight);
}
