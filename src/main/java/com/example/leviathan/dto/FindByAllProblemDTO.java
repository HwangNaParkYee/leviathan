package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindByAllProblemDTO {

    private Long problemId;
    private Long problemNumber;
    private String title;
    private Long likes;
    private Double correct;

}
