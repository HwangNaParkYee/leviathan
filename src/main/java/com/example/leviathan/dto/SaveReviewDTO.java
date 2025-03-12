package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveReviewDTO {
    private Long problemId;
    private Long memberId;
    private String answer;
    private LocalDateTime createDate;
    private boolean isRight;
}
