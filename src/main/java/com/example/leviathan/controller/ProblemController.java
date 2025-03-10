package com.example.leviathan.controller;


import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/saveProblem")
    public ResponseEntity<String> saveProblem(@RequestBody SaveProblemDTO dto){

        return problemService.saveProblem(dto);


    }


}
