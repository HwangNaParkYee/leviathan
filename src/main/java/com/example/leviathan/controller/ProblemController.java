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
public class ProblemController {

    private final ProblemService problemService;

    @PostMapping("/saveProblem")
    public ResponseEntity<String> saveProblem(@RequestBody SaveProblemDTO dto){
        return problemService.saveProblem(dto);
    }


    @CrossOrigin(origins = "http://localhost:3000")  // React 앱 주소
    @GetMapping("/findProblem/{problemId}")
    public ResponseEntity<FindProblemDTO> findProblem(@PathVariable Long problemId){

        return problemService.findProblem(problemId);
    }


    @CrossOrigin(origins = "http://localhost:3000")  // React 앱 주소
    @GetMapping("/findByAllProblem")
    public ResponseEntity<List<FindByAllProblemDTO>> findByAllProblem(){
        // 서비스에서 모든 문제를 조회하여 DTO 리스트로 반환
        List<FindByAllProblemDTO> problems = problemService.findByAllProblem();

        // List를 ResponseEntity로 감싸서 반환
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

    @CrossOrigin(origins = "http://localhost:3000")  // React 앱 주소
    @PostMapping("/submitProblem")
    public ResponseEntity<String> submitProblem(@RequestBody SubmitProblemDTO dto, HttpServletRequest header) {
        String result = problemService.submitProblem(header, dto);

        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins = "http://localhost:3000")  // React 앱 주소
    @PostMapping("/likeUpProblem/{problemId}")
    public ResponseEntity<String> likeUpProblem( @PathVariable Long problemId, HttpServletRequest header){  // 헤더에서 'Authorization' 토큰 받기
            String result = problemService.likeUpProblem(header, problemId);
            return ResponseEntity.ok(result);
    }






}
