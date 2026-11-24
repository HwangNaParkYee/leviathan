package com.example.leviathan.problem.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.problem.controller.request.SaveProblemRequest;
import com.example.leviathan.problem.controller.request.SubmitProblemRequest;
import com.example.leviathan.problem.controller.request.UpdateProblemRequest;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.problem.facade.response.FindByAllProblemResponse;
import com.example.leviathan.problem.facade.response.FindProblemResponse;
import com.example.leviathan.problem.facade.response.MyProblemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProblemController implements ProblemApi {

    private final ProblemFacade problemFacade;

    @Override
    @PostMapping("/saveProblem")
    public ResponseEntity<LeviathanResponse<Void>> saveProblem(@RequestBody SaveProblemRequest dto) {
        problemFacade.saveProblem(dto);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }

    @Override
    @GetMapping("/findProblem/{problemId}")
    public ResponseEntity<LeviathanResponse<FindProblemResponse>> findProblem(@PathVariable Long problemId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.findProblem(problemId)));
    }

    @Override
    @GetMapping("/findByProblemNumber/{problemNumber}")
    public ResponseEntity<LeviathanResponse<FindProblemResponse>> findByProblemNumber(@PathVariable Long problemNumber) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.findByProblemNumber(problemNumber)));
    }

    @Override
    @GetMapping("/public/findByAllProblem")
    public ResponseEntity<LeviathanResponse<List<FindByAllProblemResponse>>> findByAllProblem() {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.findByAllProblem()));
    }

    @Override
    @PutMapping("/updateProblem")
    public ResponseEntity<LeviathanResponse<Void>> updateProblem(@RequestBody UpdateProblemRequest dto) {
        problemFacade.updateProblem(dto);
        return ResponseEntity.ok().body(LeviathanResponse.success());
    }

    @Override
    @PostMapping("/runProblem")
    public ResponseEntity<LeviathanResponse<String>> runProblem(@RequestBody SubmitProblemRequest dto) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.runProblem(dto)));
    }

    @Override
    @PostMapping("/submitProblem")
    public ResponseEntity<LeviathanResponse<String>> submitProblem(@RequestBody SubmitProblemRequest dto, @LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.submitProblem(loginId, dto)));
    }

    @Override
    @GetMapping("/all-numbers")
    public ResponseEntity<LeviathanResponse<List<Long>>> getAllProblemNumbers() {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.getAllProblemNumbers()));
    }

    @Override
    @GetMapping("/findMyProblem")
    public ResponseEntity<LeviathanResponse<List<MyProblemResponse>>> getAllMyProblemNumbers(@LoginId String loginId) {
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.getAllMyProblemNumbers(loginId)));
    }
}
