package com.example.leviathan.problem.controller;

import com.example.leviathan.problem.facade.response.FindByAllProblemResponse;
import com.example.leviathan.problem.facade.response.FindProblemResponse;
import com.example.leviathan.problem.facade.response.MyProblemResponse;
import com.example.leviathan.problem.controller.request.SaveProblemRequest;
import com.example.leviathan.problem.controller.request.SubmitProblemRequest;
import com.example.leviathan.problem.controller.request.UpdateProblemRequest;


import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.common.annotation.LoginId;
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
    public ResponseEntity<String> saveProblem(@RequestBody SaveProblemRequest dto){
        return problemFacade.saveProblem(dto);
    }


    @GetMapping("/findProblem/{problemId}")
    public ResponseEntity<FindProblemResponse> findProblem(@PathVariable Long problemId){
        return problemFacade.findProblem(problemId);
    }

    @GetMapping("/findByProblemNumber/{problemNumber}")
    public ResponseEntity<FindProblemResponse> findByProblemNumber(@PathVariable Long problemNumber){
        return problemFacade.findByProblemNumber(problemNumber);
    }


    @GetMapping("/public/findByAllProblem")
    public ResponseEntity<List<FindByAllProblemResponse>> findByAllProblem(){
        List<FindByAllProblemResponse> problems = problemFacade.findByAllProblem();
        return ResponseEntity.ok(problems);
    }

    @PutMapping("/updateProblem")
    public ResponseEntity<String> updateProblem(@RequestBody UpdateProblemRequest dto) {
        try {
            problemFacade.updateProblem(dto);
            return ResponseEntity.ok("Problem updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/runProblem")
    public ResponseEntity<String> runProblem(@RequestBody SubmitProblemRequest dto) {
        String result = problemFacade.runProblem(dto);
        return ResponseEntity.ok(result);
    }


    @PostMapping("/submitProblem")
    public ResponseEntity<String> submitProblem(@RequestBody SubmitProblemRequest dto, @LoginId String loginId) {
        String result = problemFacade.submitProblem(loginId, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all-numbers")
    public ResponseEntity<List<Long>> getAllProblemNumbers() {
        List<Long> numbers = problemFacade.getAllProblemNumbers();
        return ResponseEntity.ok(numbers);
    }

    @GetMapping("/findMyProblem")
    public ResponseEntity<List<MyProblemResponse>> getAllMyProblemNumbers(@LoginId String loginId) {
        List<MyProblemResponse> Problems = problemFacade.getAllMyProblemNumbers(loginId);
        return ResponseEntity.ok(Problems);
    }

}
