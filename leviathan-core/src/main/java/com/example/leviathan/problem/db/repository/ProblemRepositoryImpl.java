package com.example.leviathan.problem.db.repository;

import com.example.leviathan.problem.db.entity.Problem;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

    private final ProblemJpaRepository jpaRepository;

    @Override
    public Problem save(Problem problem) {
        return jpaRepository.save(problem);
    }

    @Override
    public Optional<Problem> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Problem> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Problem findByNumber(Long number) {
        return jpaRepository.findByNumber(number);
    }

    @Override
    public List<Long> findAllProblemNumbers() {
        return jpaRepository.findAllProblemNumbers();
    }
}
