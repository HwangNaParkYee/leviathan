package com.example.leviathan.service;


import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.GptResult;
import com.example.leviathan.dto.UserPromptDTO;
import com.example.leviathan.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PromptService {

    private final ProblemRepository problemRepository;
    private final GeminiService geminiService;

    @Transactional
    public Long savePromptProblem(UserPromptDTO dto) {
        GptResult gptResult = geminiService.generateProblem(dto);

        String basePath = System.getProperty("user.dir") + "/docker_problems_file";
        File file = new File(basePath);


        String dockerFileContent = gptResult.getDockerfile();
        String shellFileContent = gptResult.getShellScript();

        Problem problem = new Problem(gptResult);
        problem.setNumber(problem.getId());

        problemRepository.save(problem);
        return problem.getId();

    }





}
