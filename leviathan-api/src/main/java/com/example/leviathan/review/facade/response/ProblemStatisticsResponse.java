package com.example.leviathan.review.facade.response;

import com.example.leviathan.review.model.ProblemStatistics;

public record ProblemStatisticsResponse(
        Long totalSubmissions,
        Long correctSubmissions,
        Long uniqueCorrectUsers
) {
    public static ProblemStatisticsResponse from(final ProblemStatistics statistics) {
        return new ProblemStatisticsResponse(
                statistics.totalSubmissions(),
                statistics.correctSubmissions(),
                statistics.uniqueCorrectUsers()
        );
    }
}
