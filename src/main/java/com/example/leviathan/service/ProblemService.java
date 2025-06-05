package com.example.leviathan.service;


import com.example.leviathan.domain.Favorite;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.dto.*;
import com.example.leviathan.repository.FavoriteRepository;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.ProblemRepository;
import com.example.leviathan.repository.ReviewRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final JwtUtility jwtUtility;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ResponseEntity<String> saveProblem(SaveProblemDTO dto) {
        // 1. 문제 번호가 이미 존재하는지 확인
        Problem existingProblem = problemRepository.findByNumber(dto.getNumber());

        // 2. 만약 해당 번호의 문제가 이미 존재하면, 에러 메시지 반환
        if (existingProblem != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem with this number already exists");
        }

        // 3. 새로운 Problem 객체 생성
        Problem problem = new Problem(dto.getNumber(), dto.getTitle(), dto.getQuestion(), dto.getHint(), dto.getLv());

        // 4. 새로운 문제 저장
        problemRepository.save(problem);

        // 5. 저장 완료 후 성공 메시지 반환
        return ResponseEntity.status(HttpStatus.CREATED).body("Problem saved successfully");
    }


    public ResponseEntity<FindProblemDTO> findProblem(Long problemId) {

        // 1. 문제를 ID로 조회
        Problem problem = problemRepository.findById(problemId).orElse(null);

        // 2. 문제를 찾지 못하면 404 Not Found 반환
        if (problem == null) {
            return ResponseEntity.notFound().build();
        }

        // 3. DTO로 변환
        FindProblemDTO findProblemDTO = new FindProblemDTO(problem, favoriteRepository.countByProblemId(problem), reviewService.createCorrectRate(problem.getId()));

        // 4. DTO를 JSON 형식으로 반환
        return ResponseEntity.ok(findProblemDTO);
    }

    public ResponseEntity<FindProblemDTO> findByProblemNumber(Long problemNumber) {
        Problem problem = problemRepository.findByNumber(problemNumber);

        if (problem == null) {
            return ResponseEntity.notFound().build();
        }
        FindProblemDTO findProblemDTO = new FindProblemDTO(problem, favoriteRepository.countByProblemId(problem), reviewService.createCorrectRate(problem.getId()));
        return ResponseEntity.ok(findProblemDTO);
    }


    public List<FindByAllProblemDTO> findByAllProblem() {
        // 모든 문제 조회
        List<Problem> problems = problemRepository.findAll();

        return problems.stream()
                .map(problem -> {
                    long likes = favoriteRepository.countByProblemId(problem);

                    // DTO 반환
                    return new FindByAllProblemDTO(
                            problem.getId(),
                            problem.getNumber(),
                            problem.getTitle(),
                            likes,  // 좋아요 개수
                            problem.getLv(),
                            reviewService.createCorrectRate(problem.getId())
                    );
                })
                .collect(Collectors.toList());
    }



    @Transactional
    public Problem updateProblem(UpdateProblemDTO dto) {
        // 문제 ID로 문제를 찾기
        Optional<Problem> optionalProblem = problemRepository.findById(dto.getProblemId());

        // 문제가 존재하지 않으면 예외 처리
        if (optionalProblem.isEmpty()) {
            throw new RuntimeException("Problem not found with id: " + dto.getProblemId());
        }

        // 문제 객체 가져오기
        Problem problem = optionalProblem.get();

        // 값이 null이 아니면 필드 업데이트
        if (dto.getProblemNumber() != null) {
            problem.setNumber(dto.getProblemNumber());
        }
        if (dto.getTitle() != null) {
            problem.setTitle(dto.getTitle());
        }
        if (dto.getQuestion() != null) {
            problem.setQuestion(dto.getQuestion());
        }
        if (dto.getHint() != null) {
            problem.setHint(dto.getHint());
        }
        if (dto.getLv() != problem.getLv()) {
            problem.setLv(dto.getLv());
        }

        // 업데이트된 문제를 저장하고 반환
        return problemRepository.save(problem);
    }

    @Transactional
    public String runProblem(HttpServletRequest header, SubmitProblemDTO dto) {
        String problemId = dto.getProblemId().toString();
        String answer = dto.getAnswer();

        // 1. 문제 존재 여부 확인
        Optional<Problem> problem = problemRepository.findById(dto.getProblemId());
        if (problem.isEmpty()) {
            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
        }

        // 2. 도커 이미지 및 컨테이너 이름 설정
        String imageName = "problem-" + problemId;
        String containerName = "problem-" + problemId + "-run";

        // 3. 사용자 명령어 따옴표 이스케이프 처리
        String sanitizedCommand = answer.replace("\"", "\\\"");

        // 4. docker run --rm 명령어 생성
        String fullCommand = String.format(

                "docker run --rm --name %s %s bash -c \"%s\"",
                containerName,
                imageName,
                sanitizedCommand
        );

        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", fullCommand});

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) output.append(line).append("\n");
            while ((line = errorReader.readLine()) != null) errorOutput.append(line).append("\n");

            int exitCode = process.waitFor();

            System.out.println("STDOUT:\n" + output);
            System.out.println("STDERR:\n" + errorOutput);

            if (exitCode != 0) {
                return "Error: Command failed (exit " + exitCode + ") - " + errorOutput.toString();
            }

            return output.toString();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during execution: " + e.getMessage();
        }
    }

    // 윈도우 로컬에서 실행 할 경우 이 메소드를 사용하세요.
    @Transactional
    public String submitProblem(HttpServletRequest header, SubmitProblemDTO dto) {
        String problemId = dto.getProblemId().toString();
        String imageName = "problem-" + problemId;

        Optional<Problem> problem = problemRepository.findById(dto.getProblemId());
        if (problem.isEmpty()) {
            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
        }

        try {
            String userCommand = dto.getAnswer().replace("\"", "\\\"");

            String os = System.getProperty("os.name").toLowerCase();
            boolean isWindows = os.contains("win");

            String command;
            if (isWindows) {
                // CMD에서는 아래처럼 "docker run 이미지 /bin/sh -c "명령어"" 형태로 실행
                command = String.format("docker run --rm -e USER_COMMANDS=\"%s\" %s", userCommand, imageName);
            } else {
                // Linux/macOS도 동일하게 쉘 명령어 직접 전달
                command = String.format("docker run --rm %s /bin/sh -c \"%s\"", imageName, userCommand);
            }

            // Process 실행 부분
            Process process = isWindows ?
                    Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command}) :
                    Runtime.getRuntime().exec(new String[]{"sh", "-c", command});

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) output.append(line).append("\n");
            while ((line = errorReader.readLine()) != null) errorOutput.append(line).append("\n");

            int exitCode = process.waitFor();
            String result = output.toString();

            // **디버그용 로그 출력 (stdout, stderr, exit code)**
            System.out.println("Docker stdout:\n" + result);
            System.out.println("Docker stderr:\n" + errorOutput.toString());
            System.out.println("Docker process exit code: " + exitCode);

            // 사용자 정보 조회
            String token = jwtUtility.extractAccessTokenFromCookie(header);
            Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));

            SaveReviewDTO reviewDTO = new SaveReviewDTO();
            reviewDTO.setProblemId(dto.getProblemId());
            reviewDTO.setMemberId(member.getId());
            reviewDTO.setAnswer(dto.getAnswer());

            if (exitCode == 3) {
                reviewDTO.setIsRight(1L);
                reviewService.saveReview(reviewDTO);
                return "맞았습니다!!";
            } else {
                reviewDTO.setIsRight(0L);
                reviewService.saveReview(reviewDTO);
                return "틀렸습니다";
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during execution: " + e.getMessage();
        }
    }


    // 문제 제출 메소드
//    @Transactional
//    public String submitProblem(HttpServletRequest header, SubmitProblemDTO dto) {
//        String problemId = dto.getProblemId().toString();
//        String imageName = "problem-" + problemId;
//
//        // 1. 문제 존재 여부 확인
//        Optional<Problem> problem = problemRepository.findById(dto.getProblemId());
//        if (problem.isEmpty()) {
//            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
//        }
//
//        try {
//            // 2. 사용자 명령어 이스케이프 처리
//            String userCommand = dto.getAnswer().replace("\"", "\\\"");
//
//            // 3. docker run 명령어 - 환경변수 USER_COMMANDS 포함
//            String command = String.format(
//                    "docker run --rm -e USER_COMMANDS=\"%s\" %s /run.sh",
//                    userCommand,
//                    imageName
//            );
//
//            // 4. docker 명령 실행
//            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
//
//            // 5. stdout, stderr 읽기
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            StringBuilder output = new StringBuilder();
//            StringBuilder errorOutput = new StringBuilder();
//            String line;
//
//            while ((line = reader.readLine()) != null) output.append(line).append("\n");
//            while ((line = errorReader.readLine()) != null) errorOutput.append(line).append("\n");
//
//            int exitCode = process.waitFor();
//            String result = output.toString();
//
//            // 6. 사용자 정보 조회
//            String token = jwtUtility.extractAccessTokenFromCookie(header);
//            Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));
//
//            // 7. 리뷰 저장
//            SaveReviewDTO reviewDTO = new SaveReviewDTO();
//            reviewDTO.setProblemId(dto.getProblemId());
//            reviewDTO.setMemberId(member.getId());
//            reviewDTO.setAnswer(dto.getAnswer());
//
//            if (result.contains("정답입니다!")) {
//                reviewDTO.setIsRight(1L);
//                reviewService.saveReview(reviewDTO);
//                return "맞았습니다!!";
//            } else {
//                reviewDTO.setIsRight(0L);
//                reviewService.saveReview(reviewDTO);
//                return "틀렸습니다";
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//            return "Error during execution: " + e.getMessage();
//        }
//    }

    @Transactional
    public String likeUpProblem(HttpServletRequest header, Long problemId) {

        String token = jwtUtility.extractAccessTokenFromCookie(header);
        // 토큰을 통해 Member 객체를 얻어오기
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));

        // problemId를 통해 Problem 객체를 찾아오기 (문제에 따라 Problem 객체가 있어야 할 수도 있음)
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        // 문제와 사용자가 즐겨찾기를 이미 했는지 확인
        Optional<Favorite> favorite = favoriteRepository.findByProblemIdAndMemberId(problem, member);

        if (favorite.isPresent()) {
            // 존재하면 삭제
            favoriteRepository.delete(favorite.get());
            return "Favorite removed.";
        } else {
            // 존재하지 않으면 추가
            Favorite newFavorite = new Favorite();
            newFavorite.setProblemId(problem);
            newFavorite.setMemberId(member);

            favoriteRepository.save(newFavorite);
            return "Favorite added.";
        }
    }

    public List<Long> getAllProblemNumbers() {
        return problemRepository.findAllProblemNumbers();
    }

    // 특정 문제의 좋아요 개수 조회
    public Long getProblemLikeCount(Long problemId) {
        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다. ID: " + problemId));

        return favoriteRepository.countByProblemId(problem);
    }

    // 로그인한 사용자가 좋아요 눌렀는지 체크
    public boolean checkProblemLike(Long problemId, HttpServletRequest header) {
        String token = jwtUtility.extractAccessTokenFromCookie(header);
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));

        if (member == null) {
            throw new RuntimeException("로그인한 사용자만 좋아요 확인이 가능합니다.");
        }

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("문제를 찾을 수 없습니다. ID: " + problemId));

        // 좋아요가 있으면 true 반환, 없으면 false 반환
        return favoriteRepository.findByProblemIdAndMemberId(problem, member).isPresent();
    }

    // 사용자가 좋아요한 문제들 조회
    public List<ProblemLikeDTO> getUserLikedProblems(HttpServletRequest header) {
        String token = jwtUtility.extractAccessTokenFromCookie(header);
        Member member = memberRepository.findByMemberId(jwtUtility.getMemberIdFromToken(token));

        if (member == null) {
            throw new RuntimeException("로그인한 사용자만 좋아요한 문제 조회가 가능합니다.");
        }

        return favoriteRepository.findByMemberId(member).stream()
                .map(favorite -> {
                    Problem problem = favorite.getProblemId();
                    return new ProblemLikeDTO(problem);
                })
                .collect(Collectors.toList());
    }
}
