package com.example.leviathan.dto;

import com.example.leviathan.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindByAllReviewDTO{
    private Long Id;
    private Long problemId;
    private Long memberId;
    private String answer;
    private LocalDateTime createDate;
    private boolean isRight;

    public static FindByAllReviewDTO fromEntity(Review review) {
        return new FindByAllReviewDTO(
                review.getId(),
                review.getProblemId().getId(),
                review.getMemberId().getId(),
                review.getAnswer(),
                review.getCreateDate(),
                review.getIsRight()
        );
    }
}