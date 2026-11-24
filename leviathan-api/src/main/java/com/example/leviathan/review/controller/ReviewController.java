package com.example.leviathan.review.controller;

import com.example.leviathan.review.dto.ActivityHistoryDTO;
import com.example.leviathan.review.dto.MyPageStatisticsDTO;
import com.example.leviathan.review.dto.ProblemStatisticsDTO;
import com.example.leviathan.review.dto.ReviewDTO;
import com.example.leviathan.review.dto.SolvedProblemDTO;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.review.facade.ReviewFacade;
import com.example.leviathan.global.security.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewFacade reviewFacade;

    //모든 리뷰 조회
    @GetMapping("/all")
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        return ResponseEntity.ok(reviewFacade.getAllReviews());
    }

    //problemId 기준 리뷰 조회
    @GetMapping("/problem/{problemId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProblemId(@PathVariable Long problemId) {
        return ResponseEntity.ok(reviewFacade.findByProblemIdReview(problemId));
    }

    //problemId + memberId 기준 리뷰 조회
    @GetMapping("/problem_member/{problemId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByProblemIdAndMemberId( @PathVariable Long problemId, @LoginId String loginId) {
        return ResponseEntity.ok(reviewFacade.findByProblemIdMemberIdReview(problemId, loginId));
    }

    //memberId 기준 리뷰 조회 (JWT 기반)
    @GetMapping("/member")
    public ResponseEntity<List<ReviewDTO>> getReviewsByMemberId(@LoginId String loginId) {
        return ResponseEntity.ok(reviewFacade.findByMemberIdReview(loginId));
    }


    // reviewId 기준 리뷰 조회
    @GetMapping("/detail/{detailId}")
    public ResponseEntity<ReviewDTO> getReviewById(
            @PathVariable Long detailId) {

        return reviewFacade.findByReviewId(detailId);
    }

    // memberId 기준 맞은 문제 조회
    @GetMapping("/my-page/solved-problems")
    public ResponseEntity<List<SolvedProblemDTO>> getSolvedProblems(@LoginId String loginId) {
        return ResponseEntity.ok(reviewFacade.findSolvedProblems(loginId));
    }

    // memberId 기준 틀린 문제 조회
    @GetMapping("/my-page/wrong-problems")
    public ResponseEntity<List<SolvedProblemDTO>> getWrongProblems(@LoginId String loginId) {
        return ResponseEntity.ok(reviewFacade.findWrongProblems(loginId));
    }

    //멤버+날짜 기준 풀었던 기록 가져오기
    @GetMapping("/my-page/activity-history")
    public ResponseEntity<List<ActivityHistoryDTO>> getActivityHistory(@LoginId String loginId) {
        return ResponseEntity.ok(reviewFacade.getActivityHistory(loginId));
    }

    //총정리 api
    @GetMapping("/my-page/statistics")
    public ResponseEntity<MyPageStatisticsDTO> getStatistics(@LoginId String loginId) {
        return ResponseEntity.ok(reviewFacade.getStatistics(loginId));
    }
    
    // 문제별 통계 조회 API
    @GetMapping("/problem-statistics/{problemId}")
    public ResponseEntity<ProblemStatisticsDTO> getProblemStatistics(@PathVariable Long problemId) {
        return ResponseEntity.ok(reviewFacade.getProblemStatistics(problemId));
    }
}