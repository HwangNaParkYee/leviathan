package com.example.leviathan.controller;


import com.example.leviathan.dto.FindByAllProblemDTO;
import com.example.leviathan.dto.FindProblemDTO;
import com.example.leviathan.dto.SaveProblemDTO;
import com.example.leviathan.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/findByAllProblem")
    public ResponseEntity<List<FindByAllProblemDTO>> findByAllProblem(){
        // 서비스에서 모든 문제를 조회하여 DTO 리스트로 반환
        List<FindByAllProblemDTO> problems = problemService.findByAllProblem();

        // List를 ResponseEntity로 감싸서 반환
        return ResponseEntity.ok(problems);
    }




}
