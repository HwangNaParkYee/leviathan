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

    public boolean checkPrompt(String topic) {
        String filterPrompt = """
당신은 리눅스 온라인 저지 문제를 만드는 AI입니다.

[역할]
- 당신의 유일한 임무는 '리눅스 명령어'와 직접 관련된 문제만을 만들 수 있도록 요청을 필터링하는 것입니다.

[사용자 요청]
"%s"

[판단 기준]
- 오직 리눅스 명령어(Bash 포함)로 터미널에서 실행 가능한 작업에 **직접적으로 관련된 요청**만 'YES'로 판단합니다.
- 예: `touch`, `cd`, `ls`, `chmod`, `grep`, `ps`, `kill`, `cat`, `mkdir`, `rm`, `top`, `whoami`, `find`, `echo` 등

[부적절 예시 — 모두 NO]
- 정치, 뉴스, 사회, 감정 표현, 문장 생성, 상식 질문, 고양이, 농담, 인공지능, GPT 질문, 자연어 처리, 수학 문제, 파이썬, 자바 등
- 리눅스와 관련된 것처럼 보이지만 **명령어 기반 문제로 만들 수 없는** 애매한 요청

[출력 규칙]
- 조건을 만족하면 "YES"
- 하나라도 어긋나면 이유 불문하고 "NO"
- 반드시 대답은 "YES" 또는 "NO"만 출력 (그 외 설명 금지)
""".formatted(topic);

        GeminiRequest request = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(filterPrompt))
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
                .getText()
                .trim();


        System.out.println("[Gemini 필터 응답] content = " + content);

        return content.equalsIgnoreCase("YES");
    }

    private String buildPrompt(String topic, int level) {
        return """
            당신은 리눅스 온라인 저지 시스템 문제를 만드는 AI입니다.\s
            
            사용자 요청 주제: "%s"
            
            아래 형식에 따라 문제를 만들어주세요.
                 - Question은 245자 이하로 간결하게 작성하세요. (DB 텍스트 제한 때문)
                 - 문제 난이도(Level)는 사용자 전달한 1~5 사이 숫자로 표현하세요.
                 - 문제에 등장하는 모든 초기 파일이나 디렉토리는 반드시 Dockerfile 내에서 생성하거나 복사하세요.
                 - 문제 설명에 “특정 명령어 사용 금지”와 같은 제한사항은 포함하지 마세요. (불필요한 혼란 방지)
                 - 사용자가 문제를 해결하기 위해 입력할 명령어는 `cd`, `mkdir`, `touch`, `ls`, `pwd` 등 일반적인 리눅스 명령어 범위 내여야 하며, 특별한 제한은 두지 마세요.
                 - run.sh는 아래 조건을 반드시 만족해야 합니다:
                   - `#!/bin/bash`로 시작하는 bash 스크립트 형식
                   - 사용자 명령어는 `$USER_COMMANDS` 변수에 문자열로 주어지고, `eval $USER_COMMANDS`로 실행함
                   - 명령어 실행 후 생성된 파일, 디렉토리, 출력 결과 등으로 정답 여부를 판단
                   - 정답일 경우 `"맞았습니다!"` 출력 후 `exit 3`
                   - 오답일 경우 `"틀렸습니다."` 출력 후 `exit 1`
                 - Dockerfile은 Ubuntu 최신 이미지 기반이며, 필요한 패키지 설치 및 작업 디렉토리 설정, 초기 파일 및 디렉토리 생성, run.sh 복사 및 실행 권한 설정, 실행 명령어 설정까지 포함하세요.
                 - 만약 해당 문제에서 초기 파일이 있다고 한다면 그 초기 파일을 만들어줘야 합니다!!!
                 - 초기 위치 아래에 dir1 폴더가 있다면 Dockerfile 내부에 RUN mkdir /home/dir1 같은 명령어를 입력해주어야 합니다.
                 
                        
            
                    
                 [Title]
                 문제 제목

                 [Question]
                 간단 명료한 문제 설명
 
                 [Hint]
                 문제 풀이에 도움이 될 힌트
 
                 [Level]
                 난이도 숫자 %d (1~5)
 
                 [Dockerfile]
                 ```Dockerfile
                 FROM ubuntu:latest
                 WORKDIR /home
                 RUN apt-get update && apt-get install -y bash
 
                 # 문제에 필요한 초기 디렉토리 및 파일 생성
                 # 예) RUN mkdir -p /home/dir1 && touch /home/dir1/file1.txt
 
                 COPY run.sh /run.sh
                 RUN chmod +x /run.sh
                 CMD ["/run.sh"]
                 ```

                 [run.sh]
                 ```
                 #!/bin/bash
                 eval $USER_COMMANDS
                 
                 # 문제 조건에 따라 정답 여부 판단 로직 작성
                 # 예) 특정 파일이나 디렉토리 존재 여부 또는 출력 결과 확인
                 
                 if [ ... ]; then
                     echo "맞았습니다!"
                     exit 3
                 else
                     echo "틀렸습니다."
                     exit 1
                 fi
                 ```
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
