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
        // ✅ 1단계: 문제 개요 생성
        String initialPrompt = buildInitialPrompt(dto.getPrompt(), dto.getLevel());

        GeminiRequest firstRequest = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(initialPrompt))
                ))
        );

        String url = baseUrl + "/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<GeminiRequest> entity = new HttpEntity<>(firstRequest, headers);

        ResponseEntity<GeminiResponse> response = restTemplate.postForEntity(url, entity, GeminiResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API 호출 실패: " + response.getStatusCode());
        }

        String firstContent = response.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();

        String title = extractBlock(firstContent, "\\[Title\\](.*?)\\[", 1).trim();
        String question = extractBlock(firstContent, "\\[Question\\](.*?)\\[", 1).trim();
        String hint = extractBlock(firstContent, "\\[Hint\\](.*?)\\[", 1).trim();
        String levelStr = extractBlock(firstContent, "\\[Level\\]\\s*(\\d+)", 1).trim();
        int level = Integer.parseInt(levelStr);

        // ✅ 2단계: Dockerfile & run.sh 생성
        String envPrompt = buildEnvironmentPrompt(question);
        GeminiRequest secondRequest = new GeminiRequest(
                List.of(new GeminiRequest.Content(
                        List.of(new GeminiRequest.Part(envPrompt))
                ))
        );

        HttpEntity<GeminiRequest> secondEntity = new HttpEntity<>(secondRequest, headers);
        ResponseEntity<GeminiResponse> secondResponse = restTemplate.postForEntity(url, secondEntity, GeminiResponse.class);

        if (!secondResponse.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Gemini API 2단계 호출 실패: " + secondResponse.getStatusCode());
        }

        String secondContent = secondResponse.getBody().getCandidates().get(0).getContent().getParts().get(0).getText();

        System.out.println("===== 전체 응답 =====");
        System.out.println(secondContent);

        String dockerfile = extractCodeBlock(secondContent, "\\[Dockerfile\\]", "dockerfile");
        System.out.println("===== Dockerfile =====");
        System.out.println(dockerfile);

        String shellScript = extractCodeBlock(secondContent, "\\[run\\.sh\\]", "bash");
        System.out.println("===== run.sh =====");
        System.out.println(shellScript);

        return new GptResult(title, question, hint, level, dockerfile, shellScript);
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

    // 미분리
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
                 - 만약 어떤 폴더 내부의 폴더들을 오름차순으로 출력하라는 문제라면 어떤 폴더 내부에 파일들을 생성해줘야만 합니다.
                        
            
                    
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



    private String buildInitialPrompt(String topic, int level) {
        return """
        당신은 리눅스 문제 출제자입니다.
        
        주제: "%s"
        난이도: %d
        
        
            아래 조건에 따라 사용자가 지정한 난이도(1~5)와 요청사항에 맞는 Linux 문제를 생성하세요.
        
            - 문제는 [Title], [Question], [Hint], [Level] 4개 항목만 출력하세요. 
            - Question은 최대 245자 이내로 간결하게 작성하세요. 
            - Level은 사용자가 준 1~5 숫자 중 하나로 설정하세요. 
            - 문제에 등장하는 모든 초기 파일과 디렉토리는 Dockerfile 내에서 반드시 생성 또는 복사해야 합니다. 
            - 문제 설명에 “특정 명령어 사용 금지” 같은 제약사항은 포함하지 마세요. 
            - 사용자가 문제 해결에 사용할 수 있는 명령어는 기본적인 Linux 명령어(`cd`, `mkdir`, `touch`, `ls`, `pwd` 등) 범위 내이며, 특별한 제한은 두지 않습니다. 
            - 문제 난이도와 요청 내용에 맞게 문제 설명과 힌트를 적절히 조절하세요. 
            - 문제 제목(Title)은 문제 내용과 난이도를 함축하는 간단한 문장으로 작성하세요. 
        

        다음 형식으로 문제의 개요만 작성하세요:

        [Title]
        문제 제목

        [Question]
        문제 본문 설명

        [Hint]
        문제 풀이 힌트

        [Level]
        난이도 (1~5 숫자만)
        """.formatted(topic, level);
    }


    private String buildEnvironmentPrompt(String question) {
        return """
    당신은 리눅스 문제 출제자입니다.

    아래는 문제 예시와 그에 따른 Dockerfile, run.sh입니다.

    [예시 Question]
    현재 작업 디렉토리에는 "a.txt" 파일이 존재합니다.
    "dir1"이라는 디렉토리가 이미 생성되어 있습니다.
    "dir1" 디렉토리 안으로 "a.txt" 파일을 이동시키는 명령어를 입력하세요.

    [예시 Dockerfile]
    ```dockerfile
    FROM ubuntu:latest
    # 문제 해결 및 정답 검증에 필요한 명령어가 포함된 패키지만 선별적으로 설치하세요( ps는 procps 를 설치해야 합니다. )
    RUN apt-get update && apt-get install -y bash
    # 초기 파일 및 디렉토리 생성 (문제 설명에 맞게 작성)
    RUN mkdir -p /home/dir1
    RUN echo "초기 내용" > /home/a.txt
    WORKDIR /home
    COPY run.sh /home/run.sh
    RUN chmod +x /home/run.sh
    CMD ["/home/run.sh"]
    ```

    [예시 run.sh]
    ```bash
    #!/bin/bash
    eval "$USER_COMMANDS"

    # 문제 조건에 맞게 정답 판단 로직 작성
    # 예) dir1 폴더가 존재하고, 그 내부에 a.txt 파일이 있어야 정답
    if [ -f ./dir1/a.txt ]; then
        echo "맞았습니다!"
        exit 3
    else
        echo "틀렸습니다."
        exit 1
    fi
    ```

    이제 아래 [Question]에 해당하는 문제에 맞는 [Dockerfile]과 [run.sh]를
    반드시 위 예시처럼 정확히 다음 형식으로 출력해주세요.

    [Dockerfile]
    ```dockerfile
    (여기에 Dockerfile 내용)
    ```

    [run.sh]
    ```bash
    (여기에 run.sh 내용)
    ```

    이외의 추가 설명이나 텍스트는 포함하지 말고, 꼭 위 형식만 정확하게 출력해주세요.

    특히 run.sh는 다음 조건을 반드시 지켜 작성하세요:

    - 사용자 명령어는 반드시 환경변수 USER_COMMANDS로 전달됩니다.
    - run.sh 내부에서 read 명령어나 사용자 입력을 기다리는 코드는 절대 사용하지 마세요.
    - run.sh는 eval "$USER_COMMANDS" 명령어로만 사용자 명령어를 실행해야 합니다.

    또한 다음과 같은 경우를 주의 하세요.
    
    - ps를 종료시키는 문제의 경우 sleep을 너무 작게 잡으면 안됩니다!! 최소 sleep 4000으로 잡아주세요.
    
    [Question]
    %s
    """.formatted(question);
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
