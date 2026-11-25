package com.example.leviathan.problem.controller;

import com.example.leviathan.problem.facade.response.ProblemLikeResponse;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.common.annotation.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem_likes")
@RequiredArgsConstructor
public class ProblemLikesController {
    private final ProblemFacade problemFacade;

    // 문제 좋아요 토글 API
    @PostMapping("/{problemId}")
    public ResponseEntity<String> toggleProblemLike(@PathVariable Long problemId, @LoginId String loginId) {
        String result = problemFacade.likeUpProblem(loginId, problemId);
        return ResponseEntity.ok(result);
    }

    // 특정 문제의 좋아요 개수 조회 API
    @GetMapping("/count/{problemId}")
    public ResponseEntity<Long> getProblemLikeCount(@PathVariable Long problemId) {
        return ResponseEntity.ok(problemFacade.getProblemLikeCount(problemId));
    }

    // 로그인한 사용자가 좋아요 눌렀는지 체크용
    @GetMapping("/likeCheck/{problemId}")
    public ResponseEntity<Boolean> checkProblemLike(@PathVariable Long problemId, @LoginId String loginId) {
        boolean likeCheck = problemFacade.checkProblemLike(problemId, loginId);
        return ResponseEntity.ok(likeCheck);
    }
    
    // 사용자가 좋아요한 문제들 조회
    @GetMapping("/user-liked")
    public ResponseEntity<List<ProblemLikeResponse>> getUserLikedProblems(@LoginId String loginId) {
        List<ProblemLikeResponse> likedProblems = problemFacade.getUserLikedProblems(loginId);
        return ResponseEntity.ok(likedProblems);
    }
}