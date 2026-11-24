package com.example.leviathan.problem.service;

import com.example.leviathan.member.db.entity.Member;
import com.example.leviathan.problem.db.entity.Favorite;
import com.example.leviathan.problem.db.repository.FavoriteRepository;
import com.example.leviathan.problem.db.entity.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    @Transactional
    public boolean toggle(Member member, Problem problem) {
        Optional<Favorite> favorite = favoriteRepository.findByProblemIdAndMemberId(problem, member);

        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            return false;
        }
        favoriteRepository.save(new Favorite(problem, member));
        return true;
    }

    public Long count(Problem problem) {
        return favoriteRepository.countByProblemId(problem);
    }

    public boolean isLiked(Member member, Problem problem) {
        return favoriteRepository.findByProblemIdAndMemberId(problem, member).isPresent();
    }

    public List<Problem> findLikedProblems(Member member) {
        return favoriteRepository.findByMemberId(member).stream()
                .map(Favorite::getProblemId)
                .toList();
    }
}
