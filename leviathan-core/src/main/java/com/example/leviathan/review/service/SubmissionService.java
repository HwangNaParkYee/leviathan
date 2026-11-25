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

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ProblemExecutor problemExecutor;
    private final ReviewRepository reviewRepository;

    public ExecutionResult run(Problem problem, String command) {
        return problemExecutor.run(problem, command);
    }

    @Transactional
    public Review submit(Member member, Problem problem, String answer) {
        ExecutionResult result = problemExecutor.judge(problem, answer);
        return reviewRepository.save(Review.submit(problem, member, answer, result.isCorrect()));
    }
}
