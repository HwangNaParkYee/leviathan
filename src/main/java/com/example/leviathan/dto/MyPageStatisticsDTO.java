package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPageStatisticsDTO {
    private Long totalSolved;
    private Long totalTried;
    private Long totalSubmissions;
}
