package com.example.leviathan.review.service;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.port.ProblemExecutor;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.db.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 문제 실행 및 제출(채점)
@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ProblemExecutor problemExecutor;
    private final ReviewRepository reviewRepository;

    // 사용자 명령어 실행 결과 확인 (채점하지 않음)
    public ExecutionResult run(Problem problem, String command) {
        return problemExecutor.run(problem, command);
    }

    // 채점하고 제출 기록을 남긴다
    @Transactional
    public Review submit(Member member, Problem problem, String answer) {
        ExecutionResult result = problemExecutor.judge(problem, answer);
        return reviewRepository.save(Review.submit(problem, member, answer, result.isCorrect()));
    }
}
