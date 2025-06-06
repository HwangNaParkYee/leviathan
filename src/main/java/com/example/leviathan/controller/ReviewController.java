package com.example.leviathan.controller;

import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.*;
import com.example.leviathan.service.MemberService;
import com.example.leviathan.service.ReviewService;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtility jwtUtility;
    private final MemberService memberService;

    //모든 리뷰 조회
    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    //problemId 기준 리뷰 조회
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProblemId(@PathVariable Long problemId) {
        return ResponseEntity.ok(reviewService.findByProblemIdReview(problemId));
    }

    //problemId + memberId 기준 리뷰 조회
    @GetMapping("/problem_member/{problemId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProblemIdAndMemberId( @PathVariable Long problemId, HttpServletRequest header) {
        return ResponseEntity.ok(reviewService.findByProblemIdMemberIdReview(problemId, header));
    }

    //memberId 기준 리뷰 조회 (JWT 기반)
    @GetMapping("/member")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMemberId(HttpServletRequest header) {
        return ResponseEntity.ok(reviewService.findByMemberIdReview(header));
    }


    // reviewId 기준 리뷰 조회
    @GetMapping("/detail/{detailId}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @PathVariable Long detailId) {

        return reviewService.findByReviewId(detailId);
    }

    // memberId 기준 맞은 문제 조회
    @GetMapping("/my-page/solved-problems")
    public ResponseEntity<List<SolvedProblemDTO>> getSolvedProblems(HttpServletRequest request) {
        return ResponseEntity.ok(reviewService.findSolvedProblems(request));
    }

    // memberId 기준 틀린 문제 조회
    @GetMapping("/my-page/wrong-problems")
    public ResponseEntity<List<SolvedProblemDTO>> getWrongProblems(HttpServletRequest request) {
        return ResponseEntity.ok(reviewService.findWrongProblems(request));
    }

    //멤버+날짜 기준 풀었던 기록 가져오기
    @GetMapping("/my-page/activity-history")
    public ResponseEntity<List<ActivityHistoryDTO>> getActivityHistory(HttpServletRequest request) {
        return ResponseEntity.ok(reviewService.getActivityHistory(request));
    }

    //총정리 api
    @GetMapping("/my-page/statistics")
    public ResponseEntity<MyPageStatisticsDTO> getStatistics(HttpServletRequest request) {
        return ResponseEntity.ok(reviewService.getStatistics(request));
    }
    
    // 문제별 통계 조회 API
    @GetMapping("/problem-statistics/{problemId}")
    public ResponseEntity<ProblemStatisticsDTO> getProblemStatistics(@PathVariable Long problemId) {
        return ResponseEntity.ok(reviewService.getProblemStatistics(problemId));
    }
}