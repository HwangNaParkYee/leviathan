package com.example.leviathan.external.docker;

import com.example.leviathan.problem.exception.ProblemErrorCode;
import com.example.leviathan.problem.exception.ProblemException;

import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.model.GeneratedProblem;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.port.ProblemExecutor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class DockerProblemExecutor implements ProblemExecutor {

    @Override
    public void prepare(Problem problem, GeneratedProblem generated) {
        String basePath = System.getProperty("user.dir") + "/docker_problems_file";
        String problemDirPath = basePath + "/" + imageName(problem);

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
            java.nio.file.Files.writeString(dockerFile.toPath(), generated.getDockerfile());

            // run.sh
            File runScript = new File(problemDir, "run.sh");
            java.nio.file.Files.writeString(runScript.toPath(), generated.getShellScript());

            // 실행 권한 부여
            runScript.setExecutable(true);

        } catch (Exception e) {
            throw new RuntimeException("파일 생성 중 오류 발생", e);
        }

        // Docker 이미지 빌드
        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "docker", "build", "-t", imageName(problem), "."
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
    }

    @Override
    public ExecutionResult run(Problem problem, String command) {
        // 도커 이미지 및 컨테이너 이름 설정
        String imageName = imageName(problem);
        String containerName = imageName + "-run";

        // 사용자 명령어 따옴표 이스케이프 처리
        String sanitizedCommand = command.replace("\"", "\\\"");

        // docker run --rm 명령어 생성
        String fullCommand = String.format(
                "docker run --rm --name %s %s bash -c \"%s\"",
                containerName,
                imageName,
                sanitizedCommand
        );

        ExecutionResult result = execute(new String[]{"sh", "-c", fullCommand});

        System.out.println("STDOUT:\n" + result.output());
        System.out.println("STDERR:\n" + result.errorOutput());

        return result;
    }

    @Override
    public ExecutionResult judge(Problem problem, String command) {
        String imageName = imageName(problem);
        String userCommand = command.replace("\"", "\\\"");

        String os = System.getProperty("os.name").toLowerCase();
        boolean isWindows = os.contains("win");

        String fullCommand;
        if (isWindows) {
            // CMD에서는 아래처럼 "docker run 이미지 /bin/sh -c "명령어"" 형태로 실행
            fullCommand = String.format("docker run --rm -e USER_COMMANDS=\"%s\" %s", userCommand, imageName);
        } else {
            // Linux/macOS도 동일하게 쉘 명령어 직접 전달
            fullCommand = String.format("docker run --rm %s /bin/sh -c \"%s\"", imageName, userCommand);
        }

        ExecutionResult result = isWindows ?
                execute(new String[]{"cmd.exe", "/c", fullCommand}) :
                execute(new String[]{"sh", "-c", fullCommand});

        // **디버그용 로그 출력 (stdout, stderr, exit code)**
        System.out.println("Docker stdout:\n" + result.output());
        System.out.println("Docker stderr:\n" + result.errorOutput());
        System.out.println("Docker process exit code: " + result.exitCode());

        return result;
    }

    private String imageName(Problem problem) {
        return "problem-" + problem.getId();
    }

    private ExecutionResult execute(String[] command) {
        try {
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) output.append(line).append("\n");
            while ((line = errorReader.readLine()) != null) errorOutput.append(line).append("\n");

            int exitCode = process.waitFor();

            return new ExecutionResult(exitCode, output.toString(), errorOutput.toString());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new ProblemException(ProblemErrorCode.PROBLEM_EXECUTION_FAILED, e);
        }
    }
}
