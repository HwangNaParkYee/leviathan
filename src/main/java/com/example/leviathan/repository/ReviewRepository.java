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

    List<Review> findByMemberIdAndIsRight(Member member, Long isRight);

    @Query("SELECT DISTINCT r.problemId.number FROM Review r " +
            "WHERE r.memberId = :member " +
            "AND r.problemId.id NOT IN (" +
            "   SELECT r2.problemId.id FROM Review r2 " +
            "   WHERE r2.memberId = :member AND r2.isRight = 1" +
            ") " +
            "AND r.isRight IS NULL")
    List<Long> findWrongProblemNumbersByMember(@Param("member") Member member);



    @Query("SELECT FUNCTION('DATE', r.createDate), COUNT(r) " +
            "FROM Review r " +
            "WHERE r.memberId = :member " +
            "GROUP BY FUNCTION('DATE', r.createDate) " +
            "ORDER BY FUNCTION('DATE', r.createDate) ASC")
    List<Object[]> countSolvedProblemsPerDay(@Param("member") Member member);

    @Query("SELECT COUNT(DISTINCT r.problemId.id) " +
            "FROM Review r " +
            "WHERE r.memberId = :member AND r.isRight = 1")
    Long countDistinctSolvedProblems(@Param("member") Member member);

    @Query("SELECT COUNT(DISTINCT r.problemId.id) " +
            "FROM Review r " +
            "WHERE r.memberId = :member")
    Long countDistinctTriedProblems(@Param("member") Member member);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.memberId = :member")
    Long countAllSubmissions(@Param("member") Member member);

}
