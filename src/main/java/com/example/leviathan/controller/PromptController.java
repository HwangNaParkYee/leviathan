package com.example.leviathan.controller;


import com.example.leviathan.dto.FindProblemDTO;
import com.example.leviathan.dto.UserPromptDTO;
import com.example.leviathan.service.ProblemService;
import com.example.leviathan.service.PromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PromptController {

    private final PromptService promptService;
    private final ProblemService problemService;


    @PostMapping("/savePromptProblem")
    public ResponseEntity<FindProblemDTO> savePromptProblem(@RequestBody UserPromptDTO dto) {
        Long problemNumber =  promptService.savePromptProblem(dto);
        return problemService.findProblem(problemNumber);
    }


}
