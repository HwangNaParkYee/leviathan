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


    @Transactional
    public ResponseEntity<String> saveProblem(SaveProblemDTO dto) {
        // 1. 문제 번호가 이미 존재하는지 확인
        Problem existingProblem = problemRepository.findByNumber(dto.getNumber());

        // 2. 만약 해당 번호의 문제가 이미 존재하면, 에러 메시지 반환
        if (existingProblem != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem with this number already exists");
        }

        // 3. 새로운 Problem 객체 생성
        Problem problem = new Problem(dto.getNumber(), dto.getTitle(), dto.getQuestion(), dto.getHint());

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

        // 업데이트된 문제를 저장하고 반환
        return problemRepository.save(problem);
    }


    public String runProblem(HttpServletRequest header, SubmitProblemDTO dto) {
        String problemId = dto.getProblemId().toString();  // problemId
        String answer = dto.getAnswer();  // answer


        System.out.println(header);
        System.out.println(dto);

        // 1. 문제 ID가 DB에 있는지 확인
        Optional<Problem> problem = problemRepository.findById(dto.getProblemId());

        if (problem.isEmpty()) {
            // 문제 ID가 DB에 없으면 에러 메시지 반환
            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
        }

        try {
            // Docker 이미지 이름을 동적으로 생성
            String imageName = "problem-" + problemId + "-test";

            // Docker 명령어를 실행할 환경 변수 설정
            String command = "docker run --rm -e USER_COMMANDS=\"" + answer + "\" " + imageName;


            System.out.println(command + "   -------------------------");

            // Docker 명령어 실행
            Process process = Runtime.getRuntime().exec(command);

            // 명령어 실행 결과 읽기 (표준 출력 + 오류 출력)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            String line;

            // 표준 출력 읽기
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 오류 출력 읽기
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // 프로세스 종료를 기다리기
            int exitCode = process.waitFor();  // 종료 코드 기다리기
            if (exitCode != 0) {
                return "Error: Docker command failed with exit code " + exitCode + " - " + errorOutput.toString();
            }

            // 명령어 실행 결과 확인
            String result = output.toString();

            return result;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during execution: " + e.getMessage();  // 실행 중 오류 발생 시
        }
    }




        // 문제 제출 메소드
    @Transactional
    public String submitProblem(HttpServletRequest header, SubmitProblemDTO dto) {
        String problemId = dto.getProblemId().toString();  // problemId
        String answer = dto.getAnswer();  // answer


        System.out.println(header);
        System.out.println(dto);

        // 1. 문제 ID가 DB에 있는지 확인
        Optional<Problem> problem = problemRepository.findById(dto.getProblemId());

        if (problem.isEmpty()) {
            // 문제 ID가 DB에 없으면 에러 메시지 반환
            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
        }

        try {
            // Docker 이미지 이름을 동적으로 생성
            String imageName = "problem-" + problemId;

            // Docker 명령어를 실행할 환경 변수 설정
            String command = "docker run --rm -e USER_COMMANDS=\"" + answer + "\" " + imageName;

            // Docker 명령어 실행
            Process process = Runtime.getRuntime().exec(command);

            // 명령어 실행 결과 읽기 (표준 출력 + 오류 출력)
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder output = new StringBuilder();
            StringBuilder errorOutput = new StringBuilder();
            String line;

            // 표준 출력 읽기
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // 오류 출력 읽기
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            // 프로세스 종료를 기다리기
            int exitCode = process.waitFor();  // 종료 코드 기다리기
            if (exitCode != 0) {
                return "Error: Docker command failed with exit code " + exitCode + " - " + errorOutput.toString();
            }

            Member member = memberService.tokenToMember(header);

            // 명령어 실행 결과 확인
            String result = output.toString();
            SaveReviewDTO saveReviewDTO = new SaveReviewDTO();
            saveReviewDTO.setProblemId(dto.getProblemId());
            saveReviewDTO.setMemberId( member.getId() );
            saveReviewDTO.setAnswer(answer);

            if (result.contains("정답입니다!")) {
                saveReviewDTO.setIsRight(1L);
                reviewService.saveReview(saveReviewDTO); // 최적화 필요 현재 두개로 나눠짐
                return "Success";  // 정답일 경우
            } else {
                reviewService.saveReview(saveReviewDTO);
                saveReviewDTO.setIsRight(0L);
                return "Fail: " + result;  // 오답일 경우, 결과도 같이 반환
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error during execution: " + e.getMessage();  // 실행 중 오류 발생 시
        }
    }


    @Transactional
    public String likeUpProblem(HttpServletRequest header, Long problemId) {


        // 토큰을 통해 Member 객체를 얻어오기
        Member member = memberService.tokenToMember(header);

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

}
