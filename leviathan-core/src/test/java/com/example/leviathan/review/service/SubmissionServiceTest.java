package com.example.leviathan.review.service;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.port.ProblemExecutor;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.db.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {

    @Mock
    private ProblemExecutor problemExecutor;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private SubmissionService submissionService;

    private final Problem problem = new Problem(1L, "title", "question", "hint", 1);
    private final Member member = new Member("testUser", "encrypted");

    @Test
    @DisplayName("채점 결과가 정답(exit code 3)이면 정답 제출 기록을 저장한다")
    void submit_Correct() {
        when(problemExecutor.judge(problem, "touch a.txt")).thenReturn(new ExecutionResult(3, "", ""));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review review = submissionService.submit(member, problem, "touch a.txt");

        assertTrue(review.isCorrect());
        assertEquals("touch a.txt", review.getAnswer());
        assertEquals(member, review.getMemberId());
        assertEquals(problem, review.getProblemId());
    }

    @Test
    @DisplayName("채점 결과가 오답이면 오답 제출 기록을 저장한다")
    void submit_Wrong() {
        when(problemExecutor.judge(problem, "ls")).thenReturn(new ExecutionResult(1, "", ""));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Review review = submissionService.submit(member, problem, "ls");

        assertFalse(review.isCorrect());
    }

    @Test
    @DisplayName("실행만 할 때는 제출 기록을 남기지 않는다")
    void run_NoReview() {
        when(problemExecutor.run(problem, "ls")).thenReturn(new ExecutionResult(0, "a.txt\n", ""));

        ExecutionResult result = submissionService.run(problem, "ls");

        assertEquals("a.txt\n", result.output());
        verify(reviewRepository, never()).save(any());
    }
}
