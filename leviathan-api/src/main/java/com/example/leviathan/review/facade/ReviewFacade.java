package com.example.leviathan.review.facade;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.member.service.MemberService;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.service.ProblemService;
import com.example.leviathan.review.model.MemberStatistics;
import com.example.leviathan.review.model.ProblemStatistics;
import com.example.leviathan.review.db.entity.Review;
import com.example.leviathan.review.model.SolvedProblem;
import com.example.leviathan.review.dto.ActivityHistoryDTO;
import com.example.leviathan.review.dto.MyPageStatisticsDTO;
import com.example.leviathan.review.dto.ProblemStatisticsDTO;
import com.example.leviathan.review.dto.ReviewDTO;
import com.example.leviathan.review.dto.SolvedProblemDTO;
import com.example.leviathan.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewFacade {

    private final ReviewService reviewService;
    private final ProblemService problemService;
    private final MemberService memberService;

    public List<ReviewDTO> getAllReviews() {
        return toReviewDTOs(reviewService.findAll());
    }

    // problemId로 리뷰 검색
    public List<ReviewDTO> findByProblemIdReview(Long problemId) {
        Problem problem = problemService.getById(problemId);
        return toReviewDTOs(reviewService.findByProblem(problem));
    }

    public List<ReviewDTO> findByProblemIdMemberIdReview(Long problemId, String loginId) {
        Member member = memberService.findByLoginId(loginId);
        Problem problem = problemService.getById(problemId);
        return toReviewDTOs(reviewService.findByProblemAndMember(problem, member));
    }

    public List<ReviewDTO> findByMemberIdReview(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toReviewDTOs(reviewService.findByMember(member));
    }

    public ResponseEntity<ReviewDTO> findByReviewId(Long reviewId) {
        return ResponseEntity.ok(ReviewDTO.fromEntity(reviewService.getById(reviewId)));
    }

    //멤버 별로 맞은 문제 가져오기
    public List<SolvedProblemDTO> findSolvedProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toSolvedProblemDTOs(reviewService.findSolvedProblems(member));
    }

    //멤버 별로 틀린 문제 가져오기
    public List<SolvedProblemDTO> findWrongProblems(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return toSolvedProblemDTOs(reviewService.findWrongProblems(member));
    }

    //잔디용
    public List<ActivityHistoryDTO> getActivityHistory(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        return reviewService.getActivityHistory(member).stream()
                .map(activity -> new ActivityHistoryDTO(activity.date(), activity.count()))
                .toList();
    }

    //총 푼 문제 갯수, 도전한 문제 갯수, 제출 횟수
    public MyPageStatisticsDTO getStatistics(String loginId) {
        Member member = memberService.findByLoginId(loginId);
        MemberStatistics statistics = reviewService.getStatistics(member);
        return new MyPageStatisticsDTO(statistics.totalSolved(), statistics.totalTried(), statistics.totalSubmissions());
    }

    public ProblemStatisticsDTO getProblemStatistics(Long problemId) {
        ProblemStatistics statistics = reviewService.getProblemStatistics(problemService.getById(problemId));
        return new ProblemStatisticsDTO(statistics.totalSubmissions(), statistics.correctSubmissions(), statistics.uniqueCorrectUsers());
    }

    private List<ReviewDTO> toReviewDTOs(List<Review> reviews) {
        return reviews.stream()
                .map(ReviewDTO::fromEntity)  // fromEntity()로 DTO 변환
                .toList();
    }

    private List<SolvedProblemDTO> toSolvedProblemDTOs(List<SolvedProblem> problems) {
        return problems.stream()
                .map(problem -> new SolvedProblemDTO(problem.problemId(), problem.problemNumber()))
                .toList();
    }
}
