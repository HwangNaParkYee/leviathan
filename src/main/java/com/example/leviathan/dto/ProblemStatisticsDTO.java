package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProblemStatisticsDTO {
    private Long totalSubmissions;      // 총 제출 횟수
    private Long correctSubmissions;    // 정답 제출 횟수
    private Long uniqueCorrectUsers;    // 맞힌 사람 수
}