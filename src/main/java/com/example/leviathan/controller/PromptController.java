package com.example.leviathan.controller;


import com.example.leviathan.dto.FindProblemDTO;
import com.example.leviathan.dto.GptResult;
import com.example.leviathan.dto.UserPromptDTO;
import com.example.leviathan.service.ProblemService;
import com.example.leviathan.service.PromptService;
import com.example.leviathan.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PromptController {

    private final PromptService promptService;
    private final ProblemService problemService;
    private final GeminiService geminiService;

    @PostMapping("/savePromptProblem")
    public ResponseEntity<FindProblemDTO> savePromptProblem(@RequestBody UserPromptDTO dto) {
        Long problemNumber =  promptService.savePromptProblem(dto);
        return problemService.findProblem(problemNumber);
    }

    @PostMapping("/prompt")
    public ResponseEntity<GptResult> generate(@RequestBody UserPromptDTO dto) {
        GptResult result = geminiService.generateProblem(dto);
        return ResponseEntity.ok(result);
    }
}
