package com.example.leviathan.dto;

import com.example.leviathan.domain.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO{
    private Long Id;
    private Long problemId;
    private Long problemNumber;
    private String memberId;
    private String answer;
    private LocalDateTime createDate;
    private Long isRight;

    public static ReviewDTO fromEntity(Review review) {
        return new ReviewDTO(
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