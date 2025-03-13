package com.example.leviathan.repository;

import com.example.leviathan.domain.Favorite;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // problemId와 memberId로 Favorite을 찾는 메소드
    Optional<Favorite> findByProblemIdAndMemberId(Problem problemId, Member memberId);
}
