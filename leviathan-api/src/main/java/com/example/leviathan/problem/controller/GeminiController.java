package com.example.leviathan.problem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gemini")
public class GeminiController {

//    private final GeminiFacade geminiFacade;
//
//    public GeminiController(GeminiFacade geminiFacade) {
//        this.geminiFacade = geminiFacade;
//    }
//
//    @GetMapping
//    public ResponseEntity<String> askGemini(@RequestParam String prompt) {
//        String result = geminiFacade.getGeminiResponse(prompt);
//        return ResponseEntity.ok(result);
//    }
}
