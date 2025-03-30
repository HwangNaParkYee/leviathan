package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActivityHistoryDTO {
    private String date;
    private Long count;
}
