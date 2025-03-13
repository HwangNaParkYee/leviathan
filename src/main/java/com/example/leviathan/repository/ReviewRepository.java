package com.example.leviathan.repository;

import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import com.example.leviathan.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByProblemId(Problem problemId);

    List<Review> findByProblemIdAndMemberId(Problem problemId, Member memberId);

    List<Review> findByMemberId(Member memberId);

}
