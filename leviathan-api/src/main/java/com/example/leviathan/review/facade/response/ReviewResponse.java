package com.example.leviathan.review.facade.response;

import com.example.leviathan.review.db.entity.Review;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long problemId,
        Long problemNumber,
        String memberId,
        String answer,
        LocalDateTime createDate,
        Long isRight
) {
    public static ReviewResponse from(final Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getProblemId().getId(),
                review.getProblemId().getNumber(),
                review.getMemberId().getMemberId(),
                review.getAnswer(),
                review.getCreateDate(),
                review.getIsRight()
        );
    }
}
