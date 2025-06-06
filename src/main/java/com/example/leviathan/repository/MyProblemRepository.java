package com.example.leviathan.repository;

import com.example.leviathan.domain.Favorite;
import com.example.leviathan.domain.Member;
import com.example.leviathan.domain.MyProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MyProblemRepository extends JpaRepository<MyProblem, Long> {

    // 특정 사용자가 만든 모든 문제 조회
    List<MyProblem> findAllByMemberId(Long member_id);
}
