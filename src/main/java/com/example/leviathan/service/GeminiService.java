package com.example.leviathan.service;

import com.example.leviathan.dto.GeminiRequest;
import com.example.leviathan.dto.GeminiResponse;
import com.example.leviathan.dto.GptResult;
import com.example.leviathan.dto.UserPromptDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GeminiService {
    private final RestTemplate restTemplate;

    @Value("${gemini.baseurl}")
    private String baseUrl;

    @Value("${gemini.api-key}")
    private String apiKey;

    // 생성자 직접 정의
    public GeminiService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }



    public GptResult generateProblem(UserPromptDTO dto) {
        String prompt = buildPrompt(dto.getPrompt());

        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(prompt))
                ))
        );

        String url = baseUrl + "/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(
                url, entity, GeminiResponse.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API 호출 실패: " + response.getStatusCode());
        }

        String content = response.getBody()
                .getCandidates().get(0)
                .getContent().getParts().get(0)
                .getText();

        return parseResponse(content);
    }

    private String buildPrompt(String topic) {
        return """
                당신은 리눅스 온라인 저지 시스템 문제를 만드는 AI입니다.

                사용자 요청 주제: "%s"

                아래 형식에 따라 문제를 만들어주세요. 각 항목은 아래 형식대로 정확하게 작성되어야 합니다:

                [Title]
                문제 제목

                [Question]
                문제 설명을 서술형으로 작성하세요.

                [Hint]
                문제 풀이를 도와줄 힌트를 적으세요.

                [Level]
                난이도 숫자만 (예: 1, 2, 3)

                [Dockerfile]
                ```Dockerfile
                FROM ubuntu:latest
                WORKDIR /home
                RUN apt-get update && apt-get install -y bash
                COPY run.sh /run.sh
                RUN chmod +x /run.sh
                CMD ["/run.sh"]
                ```

                [run.sh]
                ```bash
                #!/bin/bash
                # 여기에 채점용 스크립트 로직이 들어갑니다
                ```
                """.formatted(topic);
    }

    private GptResult parseResponse(String text) {
        String title = extractBlock(text, "\\[Title\\](.*?)\\[", 1).trim();
        String question = extractBlock(text, "\\[Question\\](.*?)\\[", 1).trim();
        String hint = extractBlock(text, "\\[Hint\\](.*?)\\[", 1).trim();
        String levelStr = extractBlock(text, "\\[Level\\](.*?)\\[", 1).trim();
        int level = Integer.parseInt(levelStr.replaceAll("[^0-9]", "").trim());

        String dockerfile = extractCodeBlock(text, "\\[Dockerfile\\]", "Dockerfile");
        String shellScript = extractCodeBlock(text, "\\[run\\.sh\\]", "bash");

        // 기본 Dockerfile 템플릿 삽입
        if (dockerfile.isBlank()) {
            dockerfile = """
                FROM ubuntu:latest
                WORKDIR /home
                RUN apt-get update && apt-get install -y bash
                COPY run.sh /run.sh
                RUN chmod +x /run.sh
                CMD ["/run.sh"]
                """.trim();
        }
        return new GptResult(title, question, hint, level, dockerfile, shellScript);
    }


    private String extractBlock(String text, String regex, int group) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(group);
        }
        return "";
    }

    private String extractCodeBlock(String text, String sectionTitle, String language) {
        String regex = sectionTitle + "\\s*```" + language + "\\s*(.*?)```";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }
}
