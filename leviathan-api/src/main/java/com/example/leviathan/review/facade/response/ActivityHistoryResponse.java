package com.example.leviathan.review.facade.response;

import com.example.leviathan.review.model.DailyActivity;

public record ActivityHistoryResponse(
        String date,
        Long count
) {
    public static ActivityHistoryResponse from(final DailyActivity activity) {
        return new ActivityHistoryResponse(activity.date(), activity.count());
    }
}
