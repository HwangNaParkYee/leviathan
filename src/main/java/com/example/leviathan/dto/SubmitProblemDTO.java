package com.example.leviathan.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitProblemDTO {

    private Long problemId;
    private String answer;

}
