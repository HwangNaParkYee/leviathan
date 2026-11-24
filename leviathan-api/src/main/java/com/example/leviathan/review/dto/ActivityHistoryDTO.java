package com.example.leviathan.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityHistoryDTO {
    private String date;
    private Long count;
}
