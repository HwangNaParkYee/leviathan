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
    public ResponseEntity<?> savePromptProblem(@RequestBody UserPromptDTO dto) {
        boolean isValid = geminiService.checkPrompt(dto.getPrompt());

        if(!isValid) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "요청하신 내용은 리눅스 실습 문제로 부적절합니다."));
        }
        Long problemNumber =  promptService.savePromptProblem(dto);
        return problemService.findProblem(problemNumber);
    }

    @PostMapping("/prompt")
    public ResponseEntity<GptResult> generate(@RequestBody UserPromptDTO dto) {
        GptResult result = geminiService.generateProblem(dto);
        return ResponseEntity.ok(result);
    }
}
