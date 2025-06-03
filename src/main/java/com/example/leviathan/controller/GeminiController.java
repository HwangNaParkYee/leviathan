package com.example.leviathan.controller;

import com.example.leviathan.service.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping
    public ResponseEntity<String> askGemini(@RequestParam String prompt) {
        String result = geminiService.getGeminiResponse(prompt);
        return ResponseEntity.ok(result);
    }
}
