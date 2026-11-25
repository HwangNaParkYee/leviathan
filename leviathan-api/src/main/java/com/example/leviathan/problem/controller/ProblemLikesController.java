package com.example.leviathan.problem.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.problem.facade.response.ProblemLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/problem_likes")
@RequiredArgsConstructor
public class ProblemLikesController {

    private final ProblemFacade problemFacade;
    @PostMapping("/{problemId}")
    public ResponseEntity<LeviathanResponse<String>> toggleProblemLike(@PathVariable Long problemId, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.likeUpProblem(loginId, problemId)));
    }
    @GetMapping("/count/{problemId}")
    public ResponseEntity<LeviathanResponse<Long>> getProblemLikeCount(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.getProblemLikeCount(problemId)));
    }
    @GetMapping("/likeCheck/{problemId}")
    public ResponseEntity<LeviathanResponse<Boolean>> checkProblemLike(@PathVariable Long problemId, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.checkProblemLike(problemId, loginId)));
    }
    @GetMapping("/user-liked")
    public ResponseEntity<LeviathanResponse<List<ProblemLikeResponse>>> getUserLikedProblems(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.getUserLikedProblems(loginId)));
    }
}
