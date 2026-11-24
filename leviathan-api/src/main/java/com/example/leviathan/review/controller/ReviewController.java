package com.example.leviathan.review.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.review.facade.ReviewFacade;
import com.example.leviathan.review.facade.response.ActivityHistoryResponse;
import com.example.leviathan.review.facade.response.MyPageStatisticsResponse;
import com.example.leviathan.review.facade.response.ProblemStatisticsResponse;
import com.example.leviathan.review.facade.response.ReviewResponse;
import com.example.leviathan.review.facade.response.SolvedProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;
    @GetMapping("/all")
    public ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getAllReviews() {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.getAllReviews()));
    }
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getReviewsByProblemId(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.findByProblemIdReview(problemId)));
    }
    @GetMapping("/problem_member/{problemId}")
    public ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getReviewsByProblemIdAndMemberId(@PathVariable Long problemId, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.findByProblemIdMemberIdReview(problemId, loginId)));
    }
    @GetMapping("/member")
    public ResponseEntity<LeviathanResponse<List<ReviewResponse>>> getReviewsByMemberId(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.findByMemberIdReview(loginId)));
    }
    @GetMapping("/detail/{detailId}")
    public ResponseEntity<LeviathanResponse<ReviewResponse>> getReviewById(@PathVariable Long detailId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.findByReviewId(detailId)));
    }
    @GetMapping("/my-page/solved-problems")
    public ResponseEntity<LeviathanResponse<List<SolvedProblemResponse>>> getSolvedProblems(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.findSolvedProblems(loginId)));
    }
    @GetMapping("/my-page/wrong-problems")
    public ResponseEntity<LeviathanResponse<List<SolvedProblemResponse>>> getWrongProblems(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.findWrongProblems(loginId)));
    }
    @GetMapping("/my-page/activity-history")
    public ResponseEntity<LeviathanResponse<List<ActivityHistoryResponse>>> getActivityHistory(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.getActivityHistory(loginId)));
    }
    @GetMapping("/my-page/statistics")
    public ResponseEntity<LeviathanResponse<MyPageStatisticsResponse>> getStatistics(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.getStatistics(loginId)));
    }
    @GetMapping("/problem-statistics/{problemId}")
    public ResponseEntity<LeviathanResponse<ProblemStatisticsResponse>> getProblemStatistics(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(reviewFacade.getProblemStatistics(problemId)));
    }
}
