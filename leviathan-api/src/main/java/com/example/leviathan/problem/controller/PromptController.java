package com.example.leviathan.problem.controller;

import com.example.leviathan.common.LeviathanResponse;
import com.example.leviathan.common.annotation.LoginId;
import com.example.leviathan.problem.controller.request.UserPromptRequest;
import com.example.leviathan.problem.facade.ProblemFacade;
import com.example.leviathan.problem.facade.PromptFacade;
import com.example.leviathan.problem.facade.response.FindProblemResponse;
import com.example.leviathan.problem.model.GeneratedProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PromptController implements PromptApi {

    private final PromptFacade promptFacade;
    private final ProblemFacade problemFacade;

    @Override
    @PostMapping("/savePromptProblem")
    public ResponseEntity<LeviathanResponse<FindProblemResponse>> savePromptProblem(@RequestBody UserPromptRequest dto, @LoginId String loginId) {
        Long problemId = promptFacade.savePromptProblem(dto, loginId);
        return ResponseEntity.ok().body(LeviathanResponse.success(problemFacade.findProblem(problemId)));
    }

    @Override
    @PostMapping("/prompt")
    public ResponseEntity<LeviathanResponse<GeneratedProblem>> generate(@RequestBody UserPromptRequest dto) {
        return ResponseEntity.ok().body(LeviathanResponse.success(promptFacade.generateProblem(dto)));
    }
}
