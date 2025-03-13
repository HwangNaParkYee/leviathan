package com.example.leviathan.service;

import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.domain.Review;
import com.example.leviathan.dto.FindByAllReviewDTO;
import com.example.leviathan.dto.SaveArticleDTO;
import com.example.leviathan.dto.SaveReviewDTO;
import com.example.leviathan.repository.ArticleRepository;
import com.example.leviathan.repository.MemberRepository;
import com.example.leviathan.repository.ProblemRepository;
import com.example.leviathan.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
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
    private final MemberRepository memberRepository;

    @Transactional
    public Review saveReview(SaveReviewDTO saveReviewDTO) {
        Problem problem = problemRepository.findById(saveReviewDTO.getProblemId())
                .orElseThrow(() -> new RuntimeException("문제 못 찾음"));

        Member member = memberRepository.findById(saveReviewDTO.getMemberId())
                .orElseThrow(() -> new RuntimeException("멤버 못 찾음"));

        Review review = new Review();
        review.setProblemId(problem);
        review.setMemberId(member);
        review.setAnswer(saveReviewDTO.getAnswer());
        review.setCreateDate(LocalDateTime.now());
        review.setIsRight(saveReviewDTO.isRight());

        return reviewRepository.save(review);
    }

    public List<FindByAllReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(FindByAllReviewDTO::fromEntity)  // fromEntity()로 DTO 변환
                .toList();
    }

}
