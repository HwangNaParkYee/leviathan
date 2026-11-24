package com.example.leviathan.review.facade;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.service.ProblemService;
import com.example.leviathan.review.model.MemberStatistics;
import com.example.leviathan.review.model.ProblemStatistics;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.model.SolvedProblem;
import com.example.leviathan.review.facade.response.ActivityHistoryResponse;
import com.example.leviathan.review.facade.response.MyPageStatisticsResponse;
import com.example.leviathan.review.facade.response.ProblemStatisticsResponse;
import com.example.leviathan.review.facade.response.ReviewResponse;
import com.example.leviathan.review.facade.response.SolvedProblemResponse;
import com.example.leviathan.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    // problemId로 리뷰 검색
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

    public ResponseEntity<ReviewResponse> findByReviewId(Long reviewId) {
        return ResponseEntity.ok(ReviewResponse.from(reviewService.getById(reviewId)));
    }

    //멤버 별로 맞은 문제 가져오기
    public List<SolvedProblemResponse> findSolvedProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toSolvedProblemResponses(reviewService.findSolvedProblems(member));
    }

    //멤버 별로 틀린 문제 가져오기
    public List<SolvedProblemResponse> findWrongProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toSolvedProblemResponses(reviewService.findWrongProblems(member));
    }

    //잔디용
    public List<ActivityHistoryResponse> getActivityHistory(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return reviewService.getActivityHistory(member).stream()
                .map(activity -> new ActivityHistoryResponse(activity.date(), activity.count()))
                .toList();
    }

    //총 푼 문제 갯수, 도전한 문제 갯수, 제출 횟수
    public MyPageStatisticsResponse getStatistics(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        MemberStatistics statistics = reviewService.getStatistics(member);
        return new MyPageStatisticsResponse(statistics.totalSolved(), statistics.totalTried(), statistics.totalSubmissions());
    }

    public ProblemStatisticsResponse getProblemStatistics(Long problemId) {
        ProblemStatistics statistics = reviewService.getProblemStatistics(problemService.getById(problemId));
        return new ProblemStatisticsResponse(statistics.totalSubmissions(), statistics.correctSubmissions(), statistics.uniqueCorrectUsers());
    }

    private List<ReviewResponse> toReviewResponses(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewResponse::from)
                .toList();
    }

    private List<SolvedProblemResponse> toSolvedProblemResponses(List<SolvedProblem> problems) {
        return problems.stream()
                .map(problem -> new SolvedProblemResponse(problem.problemId(), problem.problemNumber()))
                .toList();
    }
}
