package com.example.leviathan.service;


import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.MyProblem;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.GptResult;
import com.example.leviathan.dto.UserPromptDTO;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.MyProblemRepository;
import com.example.leviathan.repository.ProblemRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtUtility jwtUtility;
    private final MemberRepository memberRepository;
    private final MyProblemRepository myProblemRepository;

    @Transactional
    public Long savePromptProblem(UserPromptDTO dto, HttpServletRequest header) {
        GptResult gptResult = geminiService.generateProblem(dto);

        System.out.println(gptResult.getDockerfile() + gptResult.getHint() + "잘 왔습니다.");

        // 사용자 정보 조회
        String token = jwtUtility.extractAccessTokenFromCookie(header);
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));



        Problem problem = new Problem(gptResult);
        problemRepository.save(problem);

        problem.setNumber(problem.getId()); // id를 number에 복사
        problemRepository.save(problem); // 다시 저장

        MyProblem myProblem = new MyProblem(problem, member);
        myProblemRepository.save(myProblem);



        // 디렉터리 경로 설정
        String basePath = System.getProperty("user.dir") + "/docker_problems_file";
        String problemDirPath = basePath + "/problem-" + problem.getId();

        File problemDir = new File(problemDirPath);
        if (!problemDir.exists()) {
            boolean created = problemDir.mkdirs();
            if (!created) {
                throw new RuntimeException("문제 디렉토리 생성 실패: " + problemDirPath);
            }
        }

        // 파일 작성
        try {
            // Dockerfile
            File dockerFile = new File(problemDir, "Dockerfile");
            java.nio.file.Files.writeString(dockerFile.toPath(), gptResult.getDockerfile());

            // run.sh
            File runScript = new File(problemDir, "run.sh");
            java.nio.file.Files.writeString(runScript.toPath(), gptResult.getShellScript());

            // 실행 권한 부여
            runScript.setExecutable(true);

        } catch (Exception e) {
            throw new RuntimeException("파일 생성 중 오류 발생", e);
        }

        // Docker 이미지 빌드
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "docker", "build", "-t", "problem-" + problem.getId(), "."
            );

            builder.directory(problemDir); // 실행 디렉토리 설정
            builder.inheritIO(); // 로그 출력

            Process process = builder.start();
            int exitCode = process.waitFor();

            if (exitCode != 0) {
                throw new RuntimeException("Docker 이미지 빌드 실패. 종료 코드: " + exitCode);
            }
        } catch (Exception e) {
            throw new RuntimeException("Docker 빌드 중 오류 발생", e);
        }

        return problem.getId();
    }




}
