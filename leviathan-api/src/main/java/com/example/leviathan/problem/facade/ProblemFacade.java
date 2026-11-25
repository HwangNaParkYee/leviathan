package com.example.leviathan.problem.facade;

import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;
import com.example.leviathan.problem.exception.ProblemException;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.dto.FindByAllProblemDTO;
import com.example.leviathan.problem.dto.FindProblemDTO;
import com.example.leviathan.problem.dto.MyProblemDTO;
import com.example.leviathan.problem.dto.ProblemLikeDTO;
import com.example.leviathan.problem.dto.SaveProblemDTO;
import com.example.leviathan.problem.dto.SubmitProblemDTO;
import com.example.leviathan.problem.dto.UpdateProblemDTO;
import com.example.leviathan.problem.service.FavoriteService;
import com.example.leviathan.problem.service.ProblemGenerationService;
import com.example.leviathan.problem.service.ProblemService;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.service.ReviewService;
import com.example.leviathan.review.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemFacade {

    private final ProblemService problemService;
    private final FavoriteService favoriteService;
    private final ProblemGenerationService problemGenerationService;
    private final ReviewService reviewService;
    private final SubmissionService submissionService;
    private final MemberService memberService;

    // 서비스가 던진 예외를 여기서 응답으로 바꾸므로 Facade에서는 트랜잭션을 열지 않는다
    // (트랜잭션을 열면 rollback-only로 표시되어 응답 대신 500이 나간다)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public ResponseEntity<String> saveProblem(SaveProblemDTO dto) {
        try {
            problemService.create(dto.getNumber(), dto.getTitle(), dto.getQuestion(), dto.getHint(), dto.getLv());
        } catch (ProblemException e) {
            // 해당 번호의 문제가 이미 존재하면, 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Problem with this number already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Problem saved successfully");
    }

    public ResponseEntity<FindProblemDTO> findProblem(Long problemId) {
        return toFindProblemResponse(problemService.findById(problemId));
    }

    public ResponseEntity<FindProblemDTO> findByProblemNumber(Long problemNumber) {
        return toFindProblemResponse(problemService.findByNumber(problemNumber));
    }

    // 문제를 찾지 못하면 404 Not Found 반환
    private ResponseEntity<FindProblemDTO> toFindProblemResponse(Optional<Problem> problem) {
        return problem
                .map(p -> ResponseEntity.ok(new FindProblemDTO(p, favoriteService.count(p), reviewService.correctRate(p))))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public List<FindByAllProblemDTO> findByAllProblem() {
        return problemService.findAll().stream()
                .map(problem -> new FindByAllProblemDTO(
                        problem.getId(),
                        problem.getNumber(),
                        problem.getTitle(),
                        favoriteService.count(problem),  // 좋아요 개수
                        problem.getLv(),
                        reviewService.correctRate(problem)
                ))
                .toList();
    }

    @Transactional
    public Problem updateProblem(UpdateProblemDTO dto) {
        return problemService.update(dto.getProblemId(), dto.getProblemNumber(), dto.getTitle(), dto.getQuestion(), dto.getHint(), dto.getLv());
    }

    public String runProblem(SubmitProblemDTO dto) {
        // 문제 존재 여부 확인
        Optional<Problem> problem = problemService.findById(dto.getProblemId());
        if (problem.isEmpty()) {
            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
        }

        try {
            ExecutionResult result = submissionService.run(problem.get(), dto.getAnswer());

            if (!result.isSuccess()) {
                return "Error: Command failed (exit " + result.exitCode() + ") - " + result.errorOutput();
            }

            return result.output();

        } catch (ProblemException e) {
            return "Error during execution: " + e.getCause().getMessage();
        }
    }

    // 서비스가 던진 예외를 여기서 응답으로 바꾸므로 Facade에서는 트랜잭션을 열지 않는다
    // (트랜잭션을 열면 rollback-only로 표시되어 응답 대신 500이 나간다)
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String submitProblem(String loginId, SubmitProblemDTO dto) {
        Optional<Problem> problem = problemService.findById(dto.getProblemId());
        if (problem.isEmpty()) {
            return "Error: Problem with ID " + dto.getProblemId() + " does not exist.";
        }

        Member member = memberService.findByLoginId(loginId);

        try {
            Review review = submissionService.submit(member, problem.get(), dto.getAnswer());

            return review.isCorrect() ? "맞았습니다!!" : "틀렸습니다";

        } catch (ProblemException e) {
            return "Error during execution: " + e.getCause().getMessage();
        }
    }

    @Transactional
    public String likeUpProblem(String loginId, Long problemId) {
        Member member = memberService.findByLoginId(loginId);
        Problem problem = problemService.getById(problemId);

        return favoriteService.toggle(member, problem) ? "Favorite added." : "Favorite removed.";
    }

    public List<Long> getAllProblemNumbers() {
        return problemService.findAllNumbers();
    }

    // 특정 문제의 좋아요 개수 조회
    public Long getProblemLikeCount(Long problemId) {
        return favoriteService.count(problemService.getById(problemId));
    }

    // 로그인한 사용자가 좋아요 눌렀는지 체크
    public boolean checkProblemLike(Long problemId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        // 좋아요가 있으면 true 반환, 없으면 false 반환
        return favoriteService.isLiked(member, problemService.getById(problemId));
    }

    // 사용자가 좋아요한 문제들 조회
    public List<ProblemLikeDTO> getUserLikedProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return favoriteService.findLikedProblems(member).stream()
                .map(ProblemLikeDTO::new)
                .toList();
    }

    // 사용자가 생성한 문제들 조회
    public List<MyProblemDTO> getAllMyProblemNumbers(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return problemGenerationService.findGeneratedBy(member).stream()
                .map(MyProblemDTO::new)
                .toList();
    }
}
