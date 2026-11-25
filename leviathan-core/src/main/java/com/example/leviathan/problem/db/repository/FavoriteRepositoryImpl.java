package com.example.leviathan.problem.db.repository;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Favorite;
import com.example.leviathan.problem.db.entity.Problem;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final FavoriteJpaRepository jpaRepository;

    @Override
    public Favorite save(Favorite favorite) {
        return jpaRepository.save(favorite);
    }

    @Override
    public void delete(Favorite favorite) {
        jpaRepository.delete(favorite);
    }

    @Override
    public Optional<Favorite> findByProblemIdAndMemberId(Problem problemId, Member memberId) {
        return jpaRepository.findByProblemIdAndMemberId(problemId, memberId);
    }

    @Override
    public Long countByProblemId(Problem problemId) {
        return jpaRepository.countByProblemId(problemId);
    }

    @Override
    public List<Favorite> findByMemberId(Member memberId) {
        return jpaRepository.findByMemberId(memberId);
    }
}
