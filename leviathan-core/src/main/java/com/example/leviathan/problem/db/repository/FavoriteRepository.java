package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.Favorite;
import com.example.leviathan.problem.db.entity.Problem;

import com.example.leviathan.member.db.entity.Member;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository {

    Favorite save(Favorite favorite);

    void delete(Favorite favorite);

    Optional<Favorite> findByProblemIdAndMemberId(Problem problemId, Member memberId);

    Long countByProblemId(Problem problemId);

    List<Favorite> findByMemberId(Member memberId);
}
