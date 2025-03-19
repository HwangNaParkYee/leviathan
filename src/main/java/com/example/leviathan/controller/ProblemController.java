package com.example.leviathan.controller;


import com.example.leviathan.dto.*;
import com.example.leviathan.service.ProblemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}) // React 앱 주소 허용
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
        List<FindByAllProblemDTO> problems = problemService.findByAllProblem();
        return ResponseEntity.ok(problems);
    }

    @PutMapping("/updateProblem")
    public ResponseEntity<String> updateProblem(@RequestBody UpdateProblemDTO dto) {
        try {
            problemService.updateProblem(dto);
            return ResponseEntity.ok("Problem updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/runProblem")
    public ResponseEntity<String> runProblem(@RequestBody SubmitProblemDTO dto, HttpServletRequest header) {
        String result = problemService.runProblem(header, dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/submitProblem")
    public ResponseEntity<String> submitProblem(@RequestBody SubmitProblemDTO dto, HttpServletRequest header) {
        String result = problemService.submitProblem(header, dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/likeUpProblem/{problemId}")
    public ResponseEntity<String> likeUpProblem( @PathVariable Long problemId, HttpServletRequest header){
        String result = problemService.likeUpProblem(header, problemId);
        return ResponseEntity.ok(result);
    }
}
