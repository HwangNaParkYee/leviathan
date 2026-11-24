package com.example.leviathan.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPageStatisticsDTO {
    private Long totalSolved;
    private Long totalTried;
    private Long totalSubmissions;
}
