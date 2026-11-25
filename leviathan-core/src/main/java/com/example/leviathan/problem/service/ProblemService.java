package com.example.leviathan.problem.service;

import com.example.leviathan.problem.exception.ProblemErrorCode;
import com.example.leviathan.problem.exception.ProblemException;

import com.example.leviathan.problem.db.entity.Problem;
import com.example.leviathan.problem.db.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository problemRepository;

    public Optional<Problem> findById(Long problemId) {
        return problemRepository.findById(problemId);
    }

    public Problem getById(Long problemId) {
        return problemRepository.findById(problemId)
                .orElseThrow(() -> new ProblemException(ProblemErrorCode.PROBLEM_NOT_FOUND));
    }

    public Optional<Problem> findByNumber(Long number) {
        return Optional.ofNullable(problemRepository.findByNumber(number));
    }

    public List<Problem> findAll() {
        return problemRepository.findAll();
    }

    public List<Long> findAllNumbers() {
        return problemRepository.findAllProblemNumbers();
    }

    @Transactional
    public Problem create(Long number, String title, String question, String hint, int lv) {
        if (problemRepository.findByNumber(number) != null) {
            throw new ProblemException(ProblemErrorCode.DUPLICATED_PROBLEM_NUMBER);
        }
        return problemRepository.save(new Problem(number, title, question, hint, lv));
    }

    @Transactional
    public Problem update(Long problemId, Long number, String title, String question, String hint, int lv) {
        Problem problem = getById(problemId);
        problem.update(number, title, question, hint, lv);
        return problemRepository.save(problem);
    }
}
