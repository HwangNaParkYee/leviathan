package com.example.leviathan.service;

import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.domain.Review;
import com.example.leviathan.dto.*;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.ProblemRepository;
import com.example.leviathan.repository.ReviewRepository;
import com.example.leviathan.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProblemRepository problemRepository;
//    private final ProblemService problemService; 사용하지 말죠 순환참조 발생함
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final JwtUtility jwtUtility;

    @Transactional(readOnly = false)
    public Review saveReview(SaveReviewDTO saveReviewDTO) {
        Problem problem = problemRepository.findById(saveReviewDTO.getProblemId())
                .orElseThrow(() -> new RuntimeException("문제 못 찾음"));

        Member member = memberRepository.findById(saveReviewDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("멤버 못 찾음"));

        Review review = new Review(problem, member, saveReviewDTO.getAnswer(), saveReviewDTO.getIsRight());
        System.out.println(" 잘 왔어요 !!~~~~~~~~~~~~" + review);
        return reviewRepository.save(review);
    }

    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(ReviewDTO::fromEntity)  // fromEntity()로 DTO 변환
                .toList();
    }

    //Problem_id로 problem 반환하기 순환참조 때문에 ReviewService에 만들었음
    public Problem findByProblemId(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new IllegalArgumentException("문제 ID가 존재하지 않습니다: " + problemId));
    }

    // 1. problemId로 리뷰 검색
    public List<ReviewDTO> findByProblemIdReview(Long problemId) {
        Problem problem = findByProblemId(problemId);
        List<Review> reviews = reviewRepository.findByProblemId(problem);
        return reviews.stream().map(ReviewDTO::fromEntity).toList();
    }

    public List<ReviewDTO> findByProblemIdMemberIdReview(Long problemId, HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);
        Problem problem = findByProblemId(problemId);
        List<Review> reviews = reviewRepository.findByProblemIdAndMemberId(problem, member);
        return reviews.stream().map(ReviewDTO::fromEntity).toList();
    }

    public List<ReviewDTO> findByMemberIdReview(HttpServletRequest header) {
        Member member = memberService.tokenToMember(header);
        List<Review> reviews = reviewRepository.findByMemberId(member);
        return reviews.stream().map(ReviewDTO::fromEntity).toList();
    }

    public double createCorrectRate(Long problemId) {

        Problem problem = findByProblemId(problemId);
        // 전체 제출 횟수
        long totalSubmissions = reviewRepository.countByProblemId(problem);
        if (totalSubmissions == 0) {
            return 0.0; // 제출된 데이터가 없으면 정답률 0%
        }
        // 정답 횟수 (isRight = 1인 경우)
        long correctSubmissions = reviewRepository.countByProblemIdAndIsRight(problem, 1L);
        // 정답률 계산
        return ((double) correctSubmissions / totalSubmissions) * 100;
    }



    public ResponseEntity<ReviewDTO> findByReviewId(Long reviewId) {

        Review review = reviewRepository.findById(reviewId).orElseThrow( ()-> new RuntimeException("doesnt have review"));
        return ResponseEntity.ok(ReviewDTO.fromEntity(review));

    }

    //멤버 별로 맞은 문제 가져오기
    public List<SolvedProblemDTO> findSolvedProblems(HttpServletRequest request) {
        Member member = memberService.tokenToMember(request);

        List<Review> solvedReviews = reviewRepository.findByMemberIdAndIsRight(member, 1L);

        return solvedReviews.stream()
                .map(review -> new SolvedProblemDTO(
                        review.getProblemId().getId(),
                        review.getProblemId().getNumber()
                ))
                .distinct()
                .toList();
    }

    //멤버 별로 틀린 문제 가져오기
    public List<SolvedProblemDTO> findWrongProblems(HttpServletRequest request) {
        Member member = memberService.tokenToMember(request);

        return reviewRepository.findWrongProblemsDetailedByMember(member);
    }



    //잔디용
    public List<ActivityHistoryDTO> getActivityHistory(HttpServletRequest request) {
        Member member = memberService.tokenToMember(request);

        List<Object[]> results = reviewRepository.countSolvedProblemsPerDay(member);

        return results.stream()
                .map(row -> new ActivityHistoryDTO(
                        row[0].toString(),      // 날짜
                        (Long) row[1]))         // 개수
                .toList();
    }

    //총 푼 문제 갯수, 도전한 문제 갯수, 제출 횟수
    public MyPageStatisticsDTO getStatistics(HttpServletRequest request) {
        Member member = memberService.tokenToMember(request);

        Long totalSolved = reviewRepository.countDistinctSolvedProblems(member);
        Long totalTried = reviewRepository.countDistinctTriedProblems(member);
        Long totalSubmissions = reviewRepository.countAllSubmissions(member);

        return new MyPageStatisticsDTO(totalSolved, totalTried, totalSubmissions);
    }


}
