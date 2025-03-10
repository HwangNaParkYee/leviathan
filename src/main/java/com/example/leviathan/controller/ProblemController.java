package com.example.leviathan.controller;


import com.example.leviathan.dto.FindProblemDTO;
import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/saveProblem")
    public ResponseEntity<String> saveProblem(@RequestBody SaveProblemDTO dto){

        return problemService.saveProblem(dto);
    }


    @GetMapping("/findProblem/{problemId}")
    public ResponseEntity<FindProblemDTO> findProblem(@PathVariable Long problemId){

        return problemService.findProblem(problemId);
    }


}
