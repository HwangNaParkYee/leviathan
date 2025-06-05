package com.example.leviathan.service;

import com.example.leviathan.dto.GeminiRequest;
import com.example.leviathan.dto.GeminiResponse;
import com.example.leviathan.dto.GptResult;
import com.example.leviathan.dto.UserPromptDTO;
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
        String prompt = buildPrompt(dto.getPrompt(), dto.getLevel());

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

    private String buildPrompt(String topic, int level) {
        return """
                당신은 리눅스 온라인 저지 시스템 문제를 만드는 AI입니다.
                
                사용자 요청 주제: "%s"
                
                아래 형식에 따라 문제를 만들어주세요. \s
                - Question은 너무 길지 않게, 500자 이하로 작성하세요. (DB의 Text 타입 제한 때문입니다) \s
                - 문제 난이도(Level)는 사용자 전달해준 1~5 사이 숫자로 어려움 정도를 표현하세요. 
                
                [Title]
                문제 제목
                
                [Question]
                문제 설명을 서술형으로 간단하게 작성하세요.
                
                [Hint]
                문제 풀이에 도움이 되는 힌트를 적으세요.
                
                [Level]
                숫자 %d (1~5)
                
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
                다음 조건에 따라 채점 스크립트를 작성하세요:

                - `#!/bin/bash`로 시작하는 bash 스크립트 형식이어야 합니다.
                - 사용자가 입력한 명령어는 `$USER_COMMANDS`에 문자열 형태로 주어지며, `eval $USER_COMMANDS`로 실행합니다.
                - 단순히 명령어 문자열을 비교하지 말고, **명령어 실행 결과로 생성된 파일이나 출력 결과를 기준으로** 정답 여부를 판단하세요.
                - 예: "hi.txt 파일을 만들어라"는 문제일 경우, 해당 파일이 `/home/hi.txt`에 실제로 생성되었는지 `-f` 체크를 통해 확인하세요.
                - 사용자의 답이 정답일 경우 `"맞았습니다!"`를 출력하고 `exit 0`, 틀렸을 경우 `"틀렸습니다."`를 출력하고 `exit 1`을 수행하세요.
                
                예시:
                ```bash
                #!/bin/bash
                eval $USER_COMMANDS
                                
                # 파일 생성 여부 확인
                if [ -f "/home/hi.txt" ]; then
                    echo "맞았습니다!"
                    exit 0
                else
                    echo "틀렸습니다."
                    exit 1
                fi
                """.formatted(topic, level);
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
