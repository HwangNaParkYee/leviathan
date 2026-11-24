package com.example.leviathan.problem.db.entity;

import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.model.GeneratedProblem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class ProblemTest {

    @Test
    @DisplayName("null인 값은 수정하지 않는다")
    void update_SkipNull() {
        Problem problem = new Problem(1L, "title", "question", "hint", 1);

        problem.update(null, "newTitle", null, null, 3);

        assertEquals(1L, problem.getNumber());
        assertEquals("newTitle", problem.getTitle());
        assertEquals("question", problem.getQuestion());
        assertEquals("hint", problem.getHint());
        assertEquals(3, problem.getLv());
    }

    @Test
    @DisplayName("AI 생성 문제는 id를 number로 부여한다")
    void fromGenerated_AssignNumber() {
        GeneratedProblem generated = new GeneratedProblem("title", "question", "hint", 2, "FROM ubuntu", "exit 3");

        Problem problem = Problem.fromGenerated(generated);
        assertNull(problem.getNumber());
        assertEquals("title", problem.getTitle());
        assertEquals(2, problem.getLv());

        ReflectionTestUtils.setField(problem, "id", 10L);
        problem.assignNumberFromId();

        assertEquals(10L, problem.getNumber());
    }

    @Test
    @DisplayName("exit code 3이면 정답이다")
    void executionResult_Correct() {
        assertTrue(new ExecutionResult(3, "", "").isCorrect());
        assertFalse(new ExecutionResult(1, "", "").isCorrect());
        assertFalse(new ExecutionResult(0, "", "").isCorrect());
    }

    @Test
    @DisplayName("exit code 0이면 실행 성공이다")
    void executionResult_Success() {
        assertTrue(new ExecutionResult(0, "out", "").isSuccess());
        assertFalse(new ExecutionResult(127, "", "not found").isSuccess());
    }
}
