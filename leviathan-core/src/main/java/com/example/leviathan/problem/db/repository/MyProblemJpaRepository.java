package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.MyProblem;
import com.example.leviathan.member.db.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyProblemJpaRepository extends JpaRepository<MyProblem, Long> {

    // 특정 사용자가 만든 모든 문제 조회
    List<MyProblem> findAllByMemberId(Long member_id);
}
