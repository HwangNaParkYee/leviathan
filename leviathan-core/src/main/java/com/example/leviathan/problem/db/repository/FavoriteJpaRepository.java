package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.Favorite;
import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.member.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteJpaRepository extends JpaRepository<Favorite, Long> {

    // problemId와 memberId로 Favorite을 찾는 메소드
    Optional<Favorite> findByProblemIdAndMemberId(Problem problemId, Member memberId);

    // 특정 문제에 대한 Favorite 개수를 조회하는 메서드
    Long countByProblemId(Problem problemId);
    
    // 특정 사용자가 좋아요한 모든 문제 조회
    List<Favorite> findByMemberId(Member memberId);
}
