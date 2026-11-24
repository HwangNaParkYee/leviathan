package com.example.leviathan.review.facade;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.service.ProblemService;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.model.SolvedProblem;
import com.example.leviathan.review.facade.response.ActivityHistoryResponse;
import com.example.leviathan.review.facade.response.MyPageStatisticsResponse;
import com.example.leviathan.review.facade.response.ProblemStatisticsResponse;
import com.example.leviathan.review.facade.response.ReviewResponse;
import com.example.leviathan.review.facade.response.SolvedProblemResponse;
import com.example.leviathan.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewFacade {

    private final ReviewService reviewService;
    private final ProblemService problemService;
    private final MemberService memberService;

    public List<ReviewResponse> getAllReviews() {
        return toReviewResponses(reviewService.findAll());
    }

    public List<ReviewResponse> findByProblemIdReview(Long problemId) {
        Problem problem = problemService.getById(problemId);
        return toReviewResponses(reviewService.findByProblem(problem));
    }

    public List<ReviewResponse> findByProblemIdMemberIdReview(Long problemId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        Problem problem = problemService.getById(problemId);
        return toReviewResponses(reviewService.findByProblemAndMember(problem, member));
    }

    public List<ReviewResponse> findByMemberIdReview(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toReviewResponses(reviewService.findByMember(member));
    }

    public ReviewResponse findByReviewId(Long reviewId) {
        return ReviewResponse.from(reviewService.getById(reviewId));
    }

    public List<SolvedProblemResponse> findSolvedProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toSolvedProblemResponses(reviewService.findSolvedProblems(member));
    }

    public List<SolvedProblemResponse> findWrongProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toSolvedProblemResponses(reviewService.findWrongProblems(member));
    }

    public List<ActivityHistoryResponse> getActivityHistory(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return reviewService.getActivityHistory(member).stream()
                .map(ActivityHistoryResponse::from)
                .toList();
    }

    public MyPageStatisticsResponse getStatistics(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return MyPageStatisticsResponse.from(reviewService.getStatistics(member));
    }

    public ProblemStatisticsResponse getProblemStatistics(Long problemId) {
        return ProblemStatisticsResponse.from(reviewService.getProblemStatistics(problemService.getById(problemId)));
    }

    private List<ReviewResponse> toReviewResponses(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewResponse::from)
                .toList();
    }

    private List<SolvedProblemResponse> toSolvedProblemResponses(List<SolvedProblem> problems) {
        return problems.stream()
                .map(SolvedProblemResponse::from)
                .toList();
    }
}
