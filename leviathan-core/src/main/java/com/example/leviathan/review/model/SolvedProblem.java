package com.example.leviathan.review.model;

// 회원이 푼(또는 틀린) 문제
public record SolvedProblem(Long problemId, Long problemNumber) {
}
