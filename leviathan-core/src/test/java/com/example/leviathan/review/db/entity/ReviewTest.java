package com.example.leviathan.review.db.entity;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Problem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private final Problem problem = new Problem(1L, "title", "question", "hint", 1);
    private final Member member = new Member("testUser", "encrypted");

    @Test
    @DisplayName("정답 제출은 isRight가 1이다")
    void submit_Correct() {
        Review review = Review.submit(problem, member, "ls", true);

        assertTrue(review.isCorrect());
        assertEquals(1L, review.getIsRight());
        assertNotNull(review.getCreateDate());
    }

    @Test
    @DisplayName("오답 제출은 isRight가 0이다")
    void submit_Wrong() {
        Review review = Review.submit(problem, member, "ls", false);

        assertFalse(review.isCorrect());
        assertEquals(0L, review.getIsRight());
    }

    @Test
    @DisplayName("정답률 계산")
    void correctRate() {
        assertEquals(0.0, Review.correctRate(0, 0));
        assertEquals(25.0, Review.correctRate(4, 1));
        assertEquals(100.0, Review.correctRate(3, 3));
    }
}
