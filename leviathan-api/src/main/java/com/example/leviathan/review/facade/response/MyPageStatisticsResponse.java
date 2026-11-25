package com.example.leviathan.review.facade.response;

import com.example.leviathan.review.model.MemberStatistics;

public record MyPageStatisticsResponse(
        Long totalSolved,
        Long totalTried,
        Long totalSubmissions
) {
    public static MyPageStatisticsResponse from(final MemberStatistics statistics) {
        return new MyPageStatisticsResponse(
                statistics.totalSolved(),
                statistics.totalTried(),
                statistics.totalSubmissions()
        );
    }
}
