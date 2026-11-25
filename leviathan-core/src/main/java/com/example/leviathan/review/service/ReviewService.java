package com.example.leviathan.review.service;

import com.example.leviathan.review.exception.ReviewErrorCode;
import com.example.leviathan.review.exception.ReviewException;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.review.model.DailyActivity;
import com.example.leviathan.review.model.MemberStatistics;
import com.example.leviathan.review.model.ProblemStatistics;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.db.repository.ReviewRepository;
import com.example.leviathan.review.model.SolvedProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Review getById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ReviewErrorCode.REVIEW_NOT_FOUND));
    }

    public List<Review> findByProblem(Problem problem) {
        return reviewRepository.findByProblemId(problem);
    }

    public List<Review> findByProblemAndMember(Problem problem, Member member) {
        return reviewRepository.findByProblemIdAndMemberId(problem, member);
    }

    public List<Review> findByMember(Member member) {
        return reviewRepository.findByMemberId(member);
    }

    public double correctRate(Problem problem) {
        // 전체 제출 횟수
        long totalSubmissions = reviewRepository.countByProblemId(problem);

        long correctSubmissions = reviewRepository.countByProblemIdAndIsRight(problem, Review.RIGHT);

        return Review.correctRate(totalSubmissions, correctSubmissions);
    }

    //멤버 별로 맞은 문제 가져오기
    public List<SolvedProblem> findSolvedProblems(Member member) {
        return reviewRepository.findByMemberIdAndIsRight(member, Review.RIGHT).stream()
                .map(review -> new SolvedProblem(
                        review.getProblemId().getId(),
                        review.getProblemId().getNumber()
                ))
                .distinct()
                .toList();
    }

    //멤버 별로 틀린 문제 가져오기
    public List<SolvedProblem> findWrongProblems(Member member) {
        return reviewRepository.findWrongProblemsDetailedByMember(member);
    }

    //잔디용
    public List<DailyActivity> getActivityHistory(Member member) {
        return reviewRepository.countSolvedProblemsPerDay(member).stream()
                .map(row -> new DailyActivity(
                        row[0].toString(),      // 날짜
                        (Long) row[1]))         // 개수
                .toList();
    }

    //총 푼 문제 갯수, 도전한 문제 갯수, 제출 횟수
    public MemberStatistics getStatistics(Member member) {
        return new MemberStatistics(
                reviewRepository.countDistinctSolvedProblems(member),
                reviewRepository.countDistinctTriedProblems(member),
                reviewRepository.countAllSubmissions(member)
        );
    }

    public ProblemStatistics getProblemStatistics(Problem problem) {
        return new ProblemStatistics(
                reviewRepository.countByProblemId(problem),                         // 총 제출 횟수
                reviewRepository.countByProblemIdAndIsRight(problem, Review.RIGHT), // 정답 제출 횟수
                reviewRepository.countUniqueCorrectUsersByProblem(problem)          // 문제를 맞힌 사용자 수
        );
    }
}
