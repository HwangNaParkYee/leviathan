package com.example.leviathan.problem.facade;

import com.example.leviathan.member.exception.MemberErrorCode;
import com.example.leviathan.member.exception.MemberException;
import com.example.leviathan.problem.exception.ProblemErrorCode;
import com.example.leviathan.problem.exception.ProblemException;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.model.ExecutionResult;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.facade.response.FindByAllProblemResponse;
import com.example.leviathan.problem.facade.response.FindProblemResponse;
import com.example.leviathan.problem.facade.response.MyProblemResponse;
import com.example.leviathan.problem.facade.response.ProblemLikeResponse;
import com.example.leviathan.problem.controller.request.SaveProblemRequest;
import com.example.leviathan.problem.controller.request.SubmitProblemRequest;
import com.example.leviathan.problem.controller.request.UpdateProblemRequest;
import com.example.leviathan.problem.service.FavoriteService;
import com.example.leviathan.problem.service.ProblemGenerationService;
import com.example.leviathan.problem.service.ProblemService;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.service.ReviewService;
import com.example.leviathan.review.service.SubmissionService;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void saveProblem(SaveProblemRequest dto) {
        problemService.create(dto.number(), dto.title(), dto.question(), dto.hint(), dto.lv());
    }

    public FindProblemResponse findProblem(Long problemId) {
        return toFindProblemResponse(problemService.getById(problemId));
    }

    public FindProblemResponse findByProblemNumber(Long problemNumber) {
        Problem problem = problemService.findByNumber(problemNumber)
                .orElseThrow(() -> new ProblemException(ProblemErrorCode.PROBLEM_NOT_FOUND));

        return toFindProblemResponse(problem);
    }

    private FindProblemResponse toFindProblemResponse(Problem problem) {
        return FindProblemResponse.of(problem, favoriteService.count(problem), reviewService.correctRate(problem));
    }

    public List<FindByAllProblemResponse> findByAllProblem() {
        return problemService.findAll().stream()
                .map(problem -> FindByAllProblemResponse.of(
                        problem,
                        favoriteService.count(problem),
                        reviewService.correctRate(problem)
                ))
                .toList();
    }

    @Transactional
    public void updateProblem(UpdateProblemRequest dto) {
        problemService.update(dto.problemId(), dto.problemNumber(), dto.title(), dto.question(), dto.hint(), dto.lv());
    }

    public String runProblem(SubmitProblemRequest dto) {
        // 문제 존재 여부 확인
        Optional<Problem> problem = problemService.findById(dto.problemId());
        if (problem.isEmpty()) {
            return "Error: Problem with ID " + dto.problemId() + " does not exist.";
        }

        try {
            ExecutionResult result = submissionService.run(problem.get(), dto.answer());

            if (!result.isSuccess()) {
                return "Error: Command failed (exit " + result.exitCode() + ") - " + result.errorOutput();
            }

            return result.output();

        } catch (ProblemException e) {
            return "Error during execution: " + e.getCause().getMessage();
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public String submitProblem(String loginId, SubmitProblemRequest dto) {
        Optional<Problem> problem = problemService.findById(dto.problemId());
        if (problem.isEmpty()) {
            return "Error: Problem with ID " + dto.problemId() + " does not exist.";
        }

        Member member = memberService.findByLoginId(loginId);

        try {
            Review review = submissionService.submit(member, problem.get(), dto.answer());

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

    public Long getProblemLikeCount(Long problemId) {
        return favoriteService.count(problemService.getById(problemId));
    }

    public boolean checkProblemLike(Long problemId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return favoriteService.isLiked(member, problemService.getById(problemId));
    }

    public List<ProblemLikeResponse> getUserLikedProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return favoriteService.findLikedProblems(member).stream()
                .map(ProblemLikeResponse::from)
                .toList();
    }

    public List<MyProblemResponse> getAllMyProblemNumbers(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        if (member == null) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }

        return problemGenerationService.findGeneratedBy(member).stream()
                .map(MyProblemResponse::from)
                .toList();
    }
}
