package com.example.leviathan.review.db.repository;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.model.SolvedProblem;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final ReviewJpaRepository jpaRepository;

    @Override
    public Review save(Review review) {
        return jpaRepository.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Review> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public List<Object[]> findFirstReviewByProblem(Long problemId) {
        return jpaRepository.findFirstReviewByProblem(problemId);
    }

    @Override
    public List<Review> findByProblemId(Problem problemId) {
        return jpaRepository.findByProblemId(problemId);
    }

    @Override
    public List<Review> findByProblemIdAndMemberId(Problem problemId, Member memberId) {
        return jpaRepository.findByProblemIdAndMemberId(problemId, memberId);
    }

    @Override
    public List<Review> findByMemberId(Member memberId) {
        return jpaRepository.findByMemberId(memberId);
    }

    @Override
    public long countByProblemId(Problem problemId) {
        return jpaRepository.countByProblemId(problemId);
    }

    @Override
    public long countByProblemIdAndIsRight(Problem problemId, Long isRight) {
        return jpaRepository.countByProblemIdAndIsRight(problemId, isRight);
    }

    @Override
    public List<Review> findByMemberIdAndIsRight(Member member, Long isRight) {
        return jpaRepository.findByMemberIdAndIsRight(member, isRight);
    }

    @Override
    public List<Long> findWrongProblemNumbersByMember(Member member) {
        return jpaRepository.findWrongProblemNumbersByMember(member);
    }

    @Override
    public List<SolvedProblem> findWrongProblemsDetailedByMember(Member member) {
        return jpaRepository.findWrongProblemsDetailedByMember(member);
    }

    @Override
    public List<Object[]> countSolvedProblemsPerDay(Member member) {
        return jpaRepository.countSolvedProblemsPerDay(member);
    }

    @Override
    public Long countDistinctSolvedProblems(Member member) {
        return jpaRepository.countDistinctSolvedProblems(member);
    }

    @Override
    public Long countDistinctTriedProblems(Member member) {
        return jpaRepository.countDistinctTriedProblems(member);
    }

    @Override
    public Long countAllSubmissions(Member member) {
        return jpaRepository.countAllSubmissions(member);
    }

    @Override
    public Long countUniqueCorrectUsersByProblem(Problem problem) {
        return jpaRepository.countUniqueCorrectUsersByProblem(problem);
    }
}
