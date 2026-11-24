package com.example.leviathan.review.model;

// 문제별 통계: 총 제출 횟수, 정답 제출 횟수, 맞힌 사람 수
public record ProblemStatistics(Long totalSubmissions, Long correctSubmissions, Long uniqueCorrectUsers) {
}
