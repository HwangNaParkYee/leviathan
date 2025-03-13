package com.example.leviathan.repository;

import com.example.leviathan.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProblemId(Long problemId);

    List<Review> findByProblemIdAndMemberId(Long problemId, Long memberId);

    List<Review> findByMemberId(Long memberId);

}
