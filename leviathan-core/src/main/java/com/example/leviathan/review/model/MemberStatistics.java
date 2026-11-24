package com.example.leviathan.review.model;

// 회원별 통계: 푼 문제 수, 도전한 문제 수, 제출 횟수
public record MemberStatistics(Long totalSolved, Long totalTried, Long totalSubmissions) {
}
