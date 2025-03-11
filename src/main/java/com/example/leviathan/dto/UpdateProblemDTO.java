package com.example.leviathan.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProblemDTO {

    private Long problemId;
    private Long problemNumber;
    private String title;
    private String question;
    private String hint;

}
