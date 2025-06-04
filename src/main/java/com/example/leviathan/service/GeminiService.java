package com.example.leviathan.service;

import com.example.leviathan.dto.GeminiRequest;
import com.example.leviathan.dto.GeminiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeminiService {
    private final RestTemplate restTemplate;

    private final String baseUrl;

    private final String apiKey;

    public GeminiService(RestTemplateBuilder builder, @Value("${gemini.baseurl}") String baseUrl, @Value("${gemini.api-key}") String apiKey) {
        this.restTemplate = builder.build();
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
    }

    public String generateProblem(String topic) {
        String prompt = buildPrompt(topic);
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

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody()
                    .getCandidates().get(0)
                    .getContent().getParts().get(0)
                    .getText();
        }

        throw new RuntimeException("Gemini API 호출 실패: " + response.getStatusCode());
    }

    private String buildPrompt(String topic) {
        return """
                리눅스 커널 온라인 저지 시스템에서 사용할 문제를 생성해줘.

                문제 주제: "%s"

                다음 3개의 파일을 각각 코드 블록으로 생성해줘:

                ---

                2. `Dockerfile`: 반드시 아래 형식을 따를 것
                ```Dockerfile
                # 1. Ubuntu 기반 이미지 사용
                FROM ubuntu:latest

                # 2. 작업 디렉토리 설정
                WORKDIR /home

                # 3. 필수 패키지 설치
                RUN apt-get update && apt-get install -y bash

                # 4. 실행할 스크립트 복사 및 권한 부여
                COPY run.sh /run.sh
                RUN chmod +x /run.sh

                # 5. 컨테이너 실행 시 실행할 명령어
                CMD ["/run.sh"]

                check.sh: 반드시 아래 형식을 따라야 함

                #!/bin/bash

                mkdir -p /home/root

                ls

                if [ ! -z "$USER_COMMANDS" ]; then
                    echo "$USER_COMMANDS" | bash
                fi

                if [ -f "/home/root/a.txt" ] && grep -q "1234" "/home/root/a.txt"; then
                    echo "SUCCESS!"
                else
                    echo "FAIL!"
                fi
                """.formatted(topic);
    }
}
