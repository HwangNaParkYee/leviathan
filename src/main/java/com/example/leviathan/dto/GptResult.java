package com.example.leviathan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptResult {
    private String title;
    private String question;
    private String hint;
    private int level;

    private String dockerfile;
    private String shellScript;
}
