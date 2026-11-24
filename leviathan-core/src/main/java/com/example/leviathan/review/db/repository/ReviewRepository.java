package com.example.leviathan.review.db.repository;

import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.model.SolvedProblem;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository {

    Review save(Review review);

    Optional<Review> findById(Long id);

    List<Review> findAll();

    List<Object[]> findFirstReviewByProblem(Long problemId);

    List<Review> findByProblemId(Problem problemId);

    List<Review> findByProblemIdAndMemberId(Problem problemId, Member memberId);

    List<Review> findByMemberId(Member memberId);

    long countByProblemId(Problem problemId);

    long countByProblemIdAndIsRight(Problem problemId, Long isRight);

    List<Review> findByMemberIdAndIsRight(Member member, Long isRight);

    List<Long> findWrongProblemNumbersByMember(Member member);

    List<SolvedProblem> findWrongProblemsDetailedByMember(Member member);

    List<Object[]> countSolvedProblemsPerDay(Member member);

    Long countDistinctSolvedProblems(Member member);

    Long countDistinctTriedProblems(Member member);

    Long countAllSubmissions(Member member);

    Long countUniqueCorrectUsersByProblem(Problem problem);
}
