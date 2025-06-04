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
    private final RestTemplate restTemplate;  // Spring에서 제공하는 HTTP 클라이언트

    @Value("${gpt.api.url}")
    private String gptApiUrl;

    @Value("${gpt.api.key}")
    private String gptApiKey;

    @Transactional
    public Long savePromptProblem(UserPromptDTO dto) {
        GptResult gptResult = ask(dto);

        String basePath = System.getProperty("user.dir") + "/docker_problems_file";
        File file = new File(basePath);


        String dockerFileContent = gptResult.getDockerfile();
        String shellFileContent = gptResult.getShellScript();

        Problem problem = new Problem(gptResult);
        problem.setNumber(problem.getId());

        problemRepository.save(problem);
        return problem.getId();

    }



    public GptResult ask(UserPromptDTO dto) {
        // 1. GPT 요청 생성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", dto.getPrompt());
        requestBody.put("level", dto.getLevel());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(gptApiKey); // Bearer 토큰 사용

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 2. GPT API 호출
        ResponseEntity<GptResult> response = restTemplate.exchange(
                gptApiUrl,
                HttpMethod.POST,
                entity,
                GptResult.class
        );

        // 3. 결과 리턴
        return response.getBody();
    }



}
