package com.example.leviathan.problem.controller;


import com.example.leviathan.problem.dto.FindProblemDTO;
import com.example.leviathan.problem.model.GeneratedProblem;
import com.example.leviathan.problem.dto.UserPromptDTO;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.problem.facade.PromptFacade;
import com.example.leviathan.global.security.LoginId;
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

    private final PromptFacade promptFacade;
    private final ProblemFacade problemFacade;

    @PostMapping("/savePromptProblem")
    public ResponseEntity<?> savePromptProblem(@RequestBody UserPromptDTO dto, @LoginId String loginId) {
        boolean isValid = promptFacade.isValidPrompt(dto.getPrompt());

        if(!isValid) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "요청하신 내용은 리눅스 실습 문제로 부적절합니다."));
        }
        Long problemNumber =  promptFacade.savePromptProblem(dto, loginId);
        return problemFacade.findProblem(problemNumber);
    }

    @PostMapping("/prompt")
    public ResponseEntity<GeneratedProblem> generate(@RequestBody UserPromptDTO dto) {
        GeneratedProblem result = promptFacade.generateProblem(dto);
        return ResponseEntity.ok(result);
    }
}
