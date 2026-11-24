package com.example.leviathan.problem.controller;

import com.example.leviathan.problem.dto.FindByAllProblemDTO;
import com.example.leviathan.problem.dto.FindProblemDTO;
import com.example.leviathan.problem.dto.MyProblemDTO;
import com.example.leviathan.problem.dto.SaveProblemDTO;
import com.example.leviathan.problem.dto.SubmitProblemDTO;
import com.example.leviathan.problem.dto.UpdateProblemDTO;


import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.global.security.LoginId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemController {

    private final ProblemFacade problemFacade;

    @PostMapping("/saveProblem")
    public ResponseEntity<String> saveProblem(@RequestBody SaveProblemDTO dto){
        return problemFacade.saveProblem(dto);
    }


    @GetMapping("/findProblem/{problemId}")
    public ResponseEntity<FindProblemDTO> findProblem(@PathVariable Long problemId){
        return problemFacade.findProblem(problemId);
    }

    @GetMapping("/findByProblemNumber/{problemNumber}")
    public ResponseEntity<FindProblemDTO> findByProblemNumber(@PathVariable Long problemNumber){
        return problemFacade.findByProblemNumber(problemNumber);
    }


    @GetMapping("/public/findByAllProblem")
    public ResponseEntity<List<FindByAllProblemDTO>> findByAllProblem(){
        List<FindByAllProblemDTO> problems = problemFacade.findByAllProblem();
        return ResponseEntity.ok(problems);
    }

    @PutMapping("/updateProblem")
    public ResponseEntity<String> updateProblem(@RequestBody UpdateProblemDTO dto) {
        try {
            problemFacade.updateProblem(dto);
            return ResponseEntity.ok("Problem updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/runProblem")
    public ResponseEntity<String> runProblem(@RequestBody SubmitProblemDTO dto) {
        String result = problemFacade.runProblem(dto);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/submitProblem")
    public ResponseEntity<String> submitProblem(@RequestBody SubmitProblemDTO dto, @LoginId String loginId) {
        String result = problemFacade.submitProblem(loginId, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-numbers")
    public ResponseEntity<List<Long>> getAllProblemNumbers() {
        List<Long> numbers = problemFacade.getAllProblemNumbers();
        return ResponseEntity.ok(numbers);
    }

    @GetMapping("/findMyProblem")
    public ResponseEntity<List<MyProblemDTO>> getAllMyProblemNumbers(@LoginId String loginId) {
        List<MyProblemDTO> Problems = problemFacade.getAllMyProblemNumbers(loginId);
        return ResponseEntity.ok(Problems);
    }

}
