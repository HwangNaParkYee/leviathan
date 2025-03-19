package com.example.leviathan.controller;

import com.example.leviathan.domain.Member;
import com.example.leviathan.dto.ReviewDTO;
import com.example.leviathan.dto.SaveReviewDTO;
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
@RequestMapping("/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000", "https://localhost:5143"}) // React 앱 허용
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

}
